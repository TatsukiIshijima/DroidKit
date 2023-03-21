package com.tatsuki.droidkit

import android.bluetooth.BluetoothGattCharacteristic

interface DroidConnector {
  suspend fun connect()
  suspend fun disconnect()
  suspend fun discoverServices()
  suspend fun discoverCharacteristics()
  suspend fun setNotifyValue(characteristic: BluetoothGattCharacteristic)
  suspend fun setNotifyValues()
  suspend fun writeValue()
}