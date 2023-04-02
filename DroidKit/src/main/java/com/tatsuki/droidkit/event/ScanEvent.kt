package com.tatsuki.droidkit.event

import android.bluetooth.BluetoothDevice

sealed interface ScanEvent {

  object None : ScanEvent

  object OnScanning : ScanEvent

  data class OnFound(
    val device: BluetoothDevice
  ) : ScanEvent

  data class OnScanFailed(
    val errorCode: Int,
  ) : ScanEvent

  object OnStopScan : ScanEvent
}