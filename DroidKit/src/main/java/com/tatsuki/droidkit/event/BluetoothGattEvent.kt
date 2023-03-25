package com.tatsuki.droidkit.event

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

sealed interface BluetoothGattEvent {
  val gatt: BluetoothGatt?

  data class OnConnectionStateChange(
    override val gatt: BluetoothGatt?,
    val status: Int,
    val newStatus: Int,
  ) : BluetoothGattEvent

  data class OnServicesDiscovered(
    override val gatt: BluetoothGatt?,
    val status: Int,
  ) : BluetoothGattEvent

  data class OnCharacteristicRead(
    override val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?,
    val status: Int,
  ) : BluetoothGattEvent

  data class OnCharacteristicWrite(
    override val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?,
    val status: Int,
  ) : BluetoothGattEvent

  data class OnCharacteristicChanged(
    override val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?
  ) : BluetoothGattEvent
}