package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.tatsuki.droidkit.common.DroidBLE
import com.tatsuki.droidkit.common.DroidBLEException
import com.tatsuki.droidkit.event.BluetoothGattEvent
import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkit.model.DroidRawData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.util.*

class DroidConnectorImpl(
  private val context: Context,
) : DroidConnector {

  private val mutableGattEventStateFlow =
    MutableStateFlow<BluetoothGattEvent>(BluetoothGattEvent.None)

  private val isConnecting: Boolean
    get() = mutableGattEventStateFlow.value is BluetoothGattEvent.OnConnecting ||
            mutableGattEventStateFlow.value is BluetoothGattEvent.Service.OnDiscovering

  private val isDisconnected: Boolean
    get() = mutableGattEventStateFlow.value is BluetoothGattEvent.OnDisconnected ||
            mutableGattEventStateFlow.value is BluetoothGattEvent.None

  private var gatt: BluetoothGatt? = null

  private val gattCallback = object : BluetoothGattCallback() {
    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
      if (status != BluetoothGatt.GATT_SUCCESS) {
        return
      }

      if (newState == BluetoothProfile.STATE_CONNECTED) {
        this@DroidConnectorImpl.gatt = gatt
        mutableGattEventStateFlow.value = BluetoothGattEvent.OnConnected(gatt)
      }
      if (newState == BluetoothProfile.STATE_DISCONNECTED) {
        this@DroidConnectorImpl.gatt = null
        mutableGattEventStateFlow.value = BluetoothGattEvent.OnDisconnected
      }
    }

    @SuppressLint("MissingPermission")
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
      if (status != BluetoothGatt.GATT_SUCCESS) {
        return
      }

      val service = gatt?.getService(UUID.fromString(DroidBLE.W32_SERVICE_UUID)) ?: return
      mutableGattEventStateFlow.value = BluetoothGattEvent.Service.OnDiscovered(service)

      val characteristics = service.characteristics.filter { characteristic ->
        characteristic.uuid == UUID.fromString(DroidBLE.W32_CHARACTERISTIC) ||
                characteristic.uuid == UUID.fromString(DroidBLE.W32_BOARD_CONTROL_CHARACTERISTIC) ||
                characteristic.uuid == UUID.fromString(DroidBLE.W32_AUDIO_UPLOAD_CHARACTERISTIC)
      }
      // 全てのNotificationの設定が有効になれば接続完了とする
      val isAllNotificationEnabled = characteristics.map { characteristic ->
        gatt.setCharacteristicNotification(characteristic, true)
      }.all { true }

      if (!isAllNotificationEnabled) return
    }

    override fun onCharacteristicRead(
      gatt: BluetoothGatt?,
      characteristic: BluetoothGattCharacteristic?,
      status: Int
    ) {
      if (status != BluetoothGatt.GATT_SUCCESS) {
        return
      }

      mutableGattEventStateFlow.value =
        BluetoothGattEvent.Characteristic.OnRead(gatt, characteristic)
    }

    override fun onCharacteristicWrite(
      gatt: BluetoothGatt?,
      characteristic: BluetoothGattCharacteristic?,
      status: Int
    ) {
      if (status != BluetoothGatt.GATT_SUCCESS) {
        return
      }
      mutableGattEventStateFlow.value =
        BluetoothGattEvent.Characteristic.OnWrite(gatt, characteristic)
    }

    override fun onCharacteristicChanged(
      gatt: BluetoothGatt?,
      characteristic: BluetoothGattCharacteristic?
    ) {
    }
  }

  @SuppressLint("MissingPermission")
  override suspend fun connect(device: BluetoothDevice) {
    if (isConnecting) {
      disconnect()
    }
    connectInternal(device)
    val service = discoverService() ?: return
    discoverCharacteristics(service)
  }

  @SuppressLint("MissingPermission")
  private suspend fun connectInternal(device: BluetoothDevice) {
    device.connectGatt(context, false, gattCallback)
    mutableGattEventStateFlow.value = BluetoothGattEvent.OnConnecting
    mutableGattEventStateFlow.first {
      it is BluetoothGattEvent.OnConnected
    }
  }

  @SuppressLint("MissingPermission")
  private suspend fun discoverService(): BluetoothGattService? {
    if (gatt?.discoverServices() == false) {
      return null
    }
    mutableGattEventStateFlow.value = BluetoothGattEvent.Service.OnDiscovering
    mutableGattEventStateFlow.first {
      it is BluetoothGattEvent.Service.OnDiscovered
    }

    return (mutableGattEventStateFlow.value as BluetoothGattEvent.Service.OnDiscovered).service
  }

  @SuppressLint("MissingPermission")
  private suspend fun discoverCharacteristics(service: BluetoothGattService) {
    val characteristics = service.characteristics.filter { characteristic ->
      characteristic.uuid == UUID.fromString(DroidBLE.W32_CHARACTERISTIC) ||
              characteristic.uuid == UUID.fromString(DroidBLE.W32_BOARD_CONTROL_CHARACTERISTIC) ||
              characteristic.uuid == UUID.fromString(DroidBLE.W32_AUDIO_UPLOAD_CHARACTERISTIC)
    }
    // 全てのNotificationの設定が有効になれば接続完了とする
    val isAllNotificationEnabled = characteristics.map { characteristic ->
      gatt?.setCharacteristicNotification(characteristic, true)
    }.all { true }

    if (!isAllNotificationEnabled) return

    mutableGattEventStateFlow.first {
      it is BluetoothGattEvent.Characteristic.OnSetNotifications
    }
  }

  @SuppressLint("MissingPermission")
  override suspend fun disconnect() {
    gatt?.disconnect()
    gatt?.close()
    gatt = null

    mutableGattEventStateFlow.value = BluetoothGattEvent.OnDisconnected
    mutableGattEventStateFlow.first {
      it is BluetoothGattEvent.OnDisconnected
    }
  }

  @SuppressLint("MissingPermission")
  override suspend fun writeValue(command: DroidCommand) {
    checkDisconnected()

    val characteristic = gatt
      ?.getService(UUID.fromString(DroidBLE.W32_SERVICE_UUID))
      ?.getCharacteristic(UUID.fromString(DroidBLE.W32_CHARACTERISTIC)) ?: return
    val rawData = DroidRawData.create(command)
    characteristic.value = rawData.values
    gatt?.writeCharacteristic(characteristic)
    mutableGattEventStateFlow.first {
      it is BluetoothGattEvent.Characteristic.OnWrite
    }
  }

  private fun checkDisconnected() {
    if (isDisconnected) throw DroidBLEException.NotConnectedDevice()
  }
}