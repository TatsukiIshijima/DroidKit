package com.tatsuki.droidkit.event

import android.bluetooth.le.ScanResult

sealed interface ScanEvent {

  data class OnScanResult(
    val callbackType: Int,
    val result: ScanResult?
  ) : ScanEvent

  data class OnScanFailed(
    val errorCode: Int,
  ) : ScanEvent
}