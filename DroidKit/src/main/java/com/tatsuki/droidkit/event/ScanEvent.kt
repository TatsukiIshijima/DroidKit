package com.tatsuki.droidkit.event

import android.bluetooth.BluetoothDevice

sealed interface ScanEvent {

  data class OnScanResult(
    val callbackType: Int,
    val device: BluetoothDevice
  ) : ScanEvent

  data class OnScanFailed(
    val errorCode: Int,
  ) : ScanEvent
}