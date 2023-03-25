package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.tatsuki.droidkit.common.DroidBLE
import com.tatsuki.droidkit.common.DroidBLE.isReadyBle
import com.tatsuki.droidkit.event.ScanEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class DroidScannerImpl(
  private val context: Context,
  private val bluetoothAdapter: BluetoothAdapter,
) : DroidScanner {

  private var currentScanCallback: ScanCallback? = null

  @SuppressLint("MissingPermission")
  override fun startScan(timeout: Long): Flow<ScanEvent> = callbackFlow<ScanEvent> {
    if (!isReadyBle(context, bluetoothAdapter)) {
      trySend(ScanEvent.OnScanFailed(DroidBLE.BLE_NOT_READY_ERROR))
    }
    val scanFilter = ScanFilter.Builder()
      .setDeviceName(DroidBLE.W32_CONTROL_HUB)
      .setServiceUuid(
        ParcelUuid(
          UUID.fromString(DroidBLE.W32_SERVICE_UUID)
        )
      )
      .build()
    val scanSettings = ScanSettings.Builder()
      .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
      .build()
    currentScanCallback = object : ScanCallback() {
      override fun onScanResult(callbackType: Int, result: ScanResult?) {
        if (isClosedForSend) {
          return
        }
        if (result == null) {
          return
        }
        if (result.device.name != DroidBLE.W32_CONTROL_HUB) {
          return
        }
        trySend(ScanEvent.OnScanResult(callbackType, result.device))
      }

      override fun onScanFailed(errorCode: Int) {
        trySend(ScanEvent.OnScanFailed(errorCode))
      }
    }

    bluetoothAdapter.bluetoothLeScanner.startScan(
      listOf(scanFilter),
      scanSettings,
      currentScanCallback,
    )

    launch {
      delay(timeout)
      Timber.d("start scan timeout.")
      trySend(ScanEvent.OnScanFailed(DroidBLE.SCAN_FAILED_TIMEOUT_ERROR))
      close()
    }

    awaitClose {
      stopScan()
    }
  }

  @SuppressLint("MissingPermission")
  override fun stopScan() {
    if (!isReadyBle(context, bluetoothAdapter)) {
      return
    }
    if (currentScanCallback != null) {
      Timber.d("stop scan.")
      bluetoothAdapter.bluetoothLeScanner.stopScan(currentScanCallback)
    }
    currentScanCallback = null
  }
}