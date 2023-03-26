package com.tatsuki.droidkit.event

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

sealed interface BluetoothGattEvent {
  val gatt: BluetoothGatt?

  data class OnConnected(
    override val gatt: BluetoothGatt?,
  ) : BluetoothGattEvent

  data class OnConnectingTimeout(
    override val gatt: BluetoothGatt?
  ) : BluetoothGattEvent

  data class OnCharacteristicRead(
    override val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?,
  ) : BluetoothGattEvent

  data class OnCharacteristicWrite(
    override val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?,
  ) : BluetoothGattEvent

  data class OnCharacteristicChanged(
    override val gatt: BluetoothGatt?,
    val characteristic: BluetoothGattCharacteristic?
  ) : BluetoothGattEvent
}