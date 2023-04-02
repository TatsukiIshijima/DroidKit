package com.tatsuki.droidkit.event

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService

sealed interface BluetoothGattEvent {

  object None : BluetoothGattEvent

  object OnConnecting : BluetoothGattEvent

  data class OnConnected(
    val gatt: BluetoothGatt?,
  ) : BluetoothGattEvent

  object OnDisconnected : BluetoothGattEvent

  sealed interface Service : BluetoothGattEvent {
    object OnDiscovering : Service

    data class OnDiscovered(
      val service: BluetoothGattService
    ) : Service
  }

  sealed interface Characteristic : BluetoothGattEvent {

    object OnSetNotifications : Characteristic

    data class OnRead(
      val gatt: BluetoothGatt?,
      val characteristic: BluetoothGattCharacteristic?
    ) : Characteristic

    data class OnWrite(
      val gatt: BluetoothGatt?,
      val characteristic: BluetoothGattCharacteristic?
    ) : Characteristic

    data class OnChanged(
      val gatt: BluetoothGatt?,
      val characteristic: BluetoothGattCharacteristic?
    ) : Characteristic
  }
}