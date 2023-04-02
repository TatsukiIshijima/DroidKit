package com.tatsuki.droidkit

import android.bluetooth.BluetoothDevice

interface DroidScanner {
  var device: BluetoothDevice?

  suspend fun startScan()

  suspend fun stopScan()
}