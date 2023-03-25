package com.tatsuki.droidkit.common

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import timber.log.Timber

object DroidBLE {
  const val W32_CONTROL_HUB = "w32 ControlHub"
  const val W32_SERVICE_UUID = "d9d9e9e0-aa4e-4797-8151-cb41cedaf2ad"
  const val W32_CHARACTERISTIC = "d9d9e9e1-aa4e-4797-8151-cb41cedaf2ad"
  const val W32_BOARD_CONTROL_CHARACTERISTIC = "d9d9e9e2-aa4e-4797-8151-cb41cedaf2ad"
  const val W32_AUTO_UPLOAD_CHARACTERISTIC = "d9d9e9e3-aa4e-4797-8151-cb41cedaf2ad"
  const val CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"

  const val BLE_NOT_READY_ERROR = -1
  const val SCAN_FAILED_TIMEOUT_ERROR = -2
  const val CONNECT_FAILED = -3
  const val CONNECT_FAILED_TIMEOUT_ERROR = -4

  fun isReadyBle(context: Context, bluetoothAdapter: BluetoothAdapter): Boolean {
    if (!isSupportedBle(context)) {
      Timber.e("This device is not supported BLE.")
      return false
    }
    if (!isGrantedBlePermission(context)) {
      return false
    }
    if (!bluetoothAdapter.isEnabled) {
      Timber.e("BLE is disable.")
      return false
    }
    return true
  }

  private fun isSupportedBle(context: Context): Boolean {
    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
  }

  private fun isGrantedBlePermission(context: Context): Boolean {
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
        Timber.e("No ACCESS_FINE_LOCATION permission, cannot scan")
        false
      } else true
    } else
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          Timber.e("No ACCESS_COARSE_LOCATION permission, cannot scan")
          false
        } else true
      } else {
        return true
      }
  }
}