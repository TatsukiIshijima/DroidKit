package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class DroidPeripheral(
  private val context: Context,
  private val device: BluetoothDevice,
) {

  private var currentBluetoothGatt: BluetoothGatt? = null

  private val isConnectedMutableStateFlow = MutableStateFlow(false)

  private val gattCallback = object : BluetoothGattCallback() {
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
      when (newState) {
        BluetoothGatt.STATE_CONNECTED -> {
          currentBluetoothGatt = gatt
          isConnectedMutableStateFlow.value = true
        }
        BluetoothGatt.STATE_DISCONNECTED -> {
          isConnectedMutableStateFlow.value = false
        }
      }
    }
  }

  val name: String? = device.name

  val address: String? = device.address

  @RequiresApi(Build.VERSION_CODES.R)
  val alias: String? = device.alias

  suspend fun connect() = suspendCancellableCoroutine<Unit> { continuation ->
    isConnectedMutableStateFlow
      .filter { isConnected -> isConnected }
      .onEach { continuation.resume(Unit) }
    device.connectGatt(context, false, gattCallback)
  }

  suspend fun disconnect() = suspendCancellableCoroutine<Unit> { continuation ->
    isConnectedMutableStateFlow
      .filter { isConnected -> !isConnected }
      .onEach { continuation.resume(Unit) }
    currentBluetoothGatt?.disconnect()
    currentBluetoothGatt?.close()
    currentBluetoothGatt = null
  }
}