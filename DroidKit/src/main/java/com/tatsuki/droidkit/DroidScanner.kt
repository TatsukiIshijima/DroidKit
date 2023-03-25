package com.tatsuki.droidkit

import com.tatsuki.droidkit.event.ScanEvent
import kotlinx.coroutines.flow.Flow

interface DroidScanner {

  fun startScan(timeout: Long = SCAN_TIMEOUT_MILL): Flow<ScanEvent>

  fun stopScan()

  companion object {
    private const val SCAN_TIMEOUT_MILL = 5000L
  }
}