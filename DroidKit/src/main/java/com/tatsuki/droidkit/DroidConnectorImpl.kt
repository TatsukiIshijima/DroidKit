package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.tatsuki.droidkit.event.BluetoothGattEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class DroidConnectorImpl(
  private val context: Context,
  private val device: BluetoothDevice
) : DroidConnector {

  @SuppressLint("MissingPermission")
  override fun connect(timeout: Long): Flow<BluetoothGattEvent> = callbackFlow<BluetoothGattEvent> {

    var timeoutJob: Job? = null

    val gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
      override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        if (isClosedForSend) {
          return
        }

        timeoutJob?.cancel()
        timeoutJob = null

        if (status == BluetoothGatt.GATT_SUCCESS) {
          val gattEvent = when (newState) {
            BluetoothProfile.STATE_CONNECTED -> BluetoothGattEvent.OnConnected(gatt)
            BluetoothProfile.STATE_DISCONNECTED -> BluetoothGattEvent.OnDisconnected(gatt)
            else -> null
          } ?: return
          trySend(gattEvent)
        }
      }

      override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
          trySend(BluetoothGattEvent.OnServicesDiscovered(gatt))
        }
      }

      override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
      ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
          trySend(BluetoothGattEvent.OnCharacteristicRead(gatt, characteristic))
        }
      }

      override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
      ) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
          trySend(BluetoothGattEvent.OnCharacteristicWrite(gatt, characteristic))
        }
      }

      override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
      ) {
        trySend(BluetoothGattEvent.OnCharacteristicChanged(gatt, characteristic))
      }
    })

    timeoutJob = launch {
      delay(timeout)
      Timber.d("connect timeout.")
      trySend(BluetoothGattEvent.OnConnectingTimeout(gatt))
      close()
    }

    awaitClose {
      disconnect(gatt)
    }
  }

  @SuppressLint("MissingPermission")
  override fun disconnect(gatt: BluetoothGatt?) {
    gatt?.disconnect()
    gatt?.close()
  }
}