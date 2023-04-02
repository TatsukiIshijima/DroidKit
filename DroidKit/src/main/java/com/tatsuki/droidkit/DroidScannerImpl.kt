package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import com.tatsuki.droidkit.common.DroidBLE
import com.tatsuki.droidkit.event.ScanEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.util.*

class DroidScannerImpl(
  private val bluetoothAdapter: BluetoothAdapter,
) : DroidScanner {

  override var device: BluetoothDevice? = null

  private val mutableScanEventStateFlow = MutableStateFlow<ScanEvent>(ScanEvent.None)

  private val isScanning: Boolean
    get() = mutableScanEventStateFlow.value is ScanEvent.OnScanning

  private val scanCallback = object : ScanCallback() {
    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
      if (result == null) {
        return
      }
      if (result.device.name != DroidBLE.W32_CONTROL_HUB) {
        return
      }
      device = result.device
      mutableScanEventStateFlow.value = ScanEvent.OnFound(result.device)
    }

    override fun onScanFailed(errorCode: Int) {
      mutableScanEventStateFlow.value = ScanEvent.OnScanFailed(errorCode)
    }
  }

  @SuppressLint("MissingPermission")
  override suspend fun startScan() {
    stopScan()

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

    bluetoothAdapter.bluetoothLeScanner.startScan(
      listOf(scanFilter),
      scanSettings,
      scanCallback,
    )

    mutableScanEventStateFlow.value = ScanEvent.OnScanning

    mutableScanEventStateFlow.first {
      it is ScanEvent.OnFound
    }

    stopScan()
  }

  @SuppressLint("MissingPermission")
  override suspend fun stopScan() {
    bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
    mutableScanEventStateFlow.value = ScanEvent.OnStopScan
    mutableScanEventStateFlow.first {
      it is ScanEvent.OnStopScan
    }
  }
}