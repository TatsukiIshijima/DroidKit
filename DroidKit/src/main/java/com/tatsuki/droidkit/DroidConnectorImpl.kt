package com.tatsuki.droidkit

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import com.tatsuki.droidkit.DroidException.NotReadyException
import com.tatsuki.droidkit.DroidException.ScanFailedException
import java.util.*
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import timber.log.Timber

class DroidConnectorImpl(
  private val context: Context,
) : DroidConnector {

  private var currentScanCallback: ScanCallback? = null

  private val bluetoothAdapter: BluetoothAdapter by lazy {
    (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
  }

  private var currentPeripheral: DroidPeripheral? = null

  private fun isReadyBle(): Boolean {
    if (!isSupportedBle()) {
      Timber.e(TAG, "This device is not supported BLE.")
      return false
    }
    if (!isGrantedBlePermission()) {
      return false
    }
    if (!bluetoothAdapter.isEnabled) {
      Timber.e(TAG, "BLE is disable.")
      return false
    }
    return true
  }

  private fun isSupportedBle(): Boolean {
    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
  }

  private fun isGrantedBlePermission(): Boolean {
    val targetSdkVersion = context.applicationInfo.targetSdkVersion
    // Android 12
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && targetSdkVersion >= Build.VERSION_CODES.S) {
      if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
        throw SecurityException("App does not have BLUETOOTH_SCAN permission, cannot start scan")
      }
      return if (context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        throw SecurityException("App does not have BLUETOOTH_CONNECT permission, cannot connect")
      } else true
      // Android 10
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && targetSdkVersion >= Build.VERSION_CODES.Q) {
      if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        Timber.e(TAG, "No ACCESS_FINE_LOCATION permission, cannot scan")
        false
      } else true
    } else
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          Timber.e(TAG, "No ACCESS_COARSE_LOCATION permission, cannot scan")
          false
        } else true
      } else {
        return true
      }
  }

  @SuppressLint("MissingPermission")
  private suspend fun startScan() = suspendCancellableCoroutine<DroidPeripheral> { continuation ->
    if (!isReadyBle()) {
      continuation.resumeWithException(NotReadyException)
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
        if (result == null) {
          return
        } else {
          val peripheral = DroidPeripheral(context, result.device)
          if (peripheral.name != DroidBLE.W32_CONTROL_HUB) {
            return
          }
          continuation.resume(peripheral)
        }
      }

      override fun onScanFailed(errorCode: Int) {
        continuation.resumeWithException(ScanFailedException(errorCode))
      }
    }

    bluetoothAdapter.bluetoothLeScanner.startScan(
      listOf(scanFilter),
      scanSettings,
      currentScanCallback,
    )
  }

  @SuppressLint("MissingPermission")
  private fun stopScan() {
    if (!isReadyBle()) {
      return
    }
    if (currentScanCallback != null) {
      bluetoothAdapter.bluetoothLeScanner.stopScan(currentScanCallback)
    }
    currentScanCallback = null
  }

  @SuppressLint("MissingPermission")
  override suspend fun connect() {
    try {
      val peripheral = withTimeout(30000L) {
        startScan()
      }
      withTimeout(30000L) {
        peripheral.connect()
      }
      currentPeripheral = peripheral
    } catch (e: TimeoutCancellationException) {
      stopScan()
    } catch (e: DroidException) {
      stopScan()
    }
  }

  override suspend fun disconnect() {
    try {
      currentPeripheral?.disconnect()
    } catch (e: TimeoutCancellationException) {
      // nothing
    }
  }

  override suspend fun discoverServices() {
    TODO("Not yet implemented")
  }

  override suspend fun discoverCharacteristics() {
    TODO("Not yet implemented")
  }

  override suspend fun setNotifyValue(characteristic: BluetoothGattCharacteristic) {
    TODO("Not yet implemented")
  }

  override suspend fun setNotifyValues() {
    TODO("Not yet implemented")
  }

  override suspend fun writeValue() {
    TODO("Not yet implemented")
  }

  companion object {
    private val TAG = DroidConnector::class.java.simpleName
  }
}