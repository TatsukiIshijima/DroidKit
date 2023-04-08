package com.tatsuki.droidkitapp

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tatsuki.droidkit.*
import com.tatsuki.droidkit.common.DroidBLE
import com.tatsuki.droidkit.model.DroidCommand
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class MainViewModel(
  application: Application
) : AndroidViewModel(application) {

  private val bluetoothAdapter: BluetoothAdapter =
    (application.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
  private val droidScanner: DroidScanner = DroidScannerImpl(bluetoothAdapter)
  private val droidConnector: DroidConnector = DroidConnectorImpl(application)
  private val droidOperator: DroidOperator = DroidOperatorImpl(droidConnector)

  fun connect(
    timeoutMill: Long = 5000L
  ) {
    viewModelScope.launch {
      try {
        if (!DroidBLE.isReadyBle(getApplication(), bluetoothAdapter)) {
          throw IllegalStateException()
        }
        withTimeout(timeoutMill) {
          droidScanner.startScan()
          val droidDevice = droidScanner.device ?: return@withTimeout
          droidConnector.connect(droidDevice)
        }
      } catch (e: Exception) {
        ensureActive()
        droidScanner.stopScan()
        droidConnector.disconnect()
        Timber.e(e)
      }
    }
  }

  fun disconnect() {
    viewModelScope.launch {
      try {
        if (!DroidBLE.isReadyBle(getApplication(), bluetoothAdapter)) {
          throw IllegalStateException()
        }
        droidConnector.disconnect()
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun changeLEDColor(red: Int, green: Int, blue: Int) {
    viewModelScope.launch {
      try {
        droidOperator.changeLEDColor(red, green, blue)
      } catch (e: Exception) {
        ensureActive()
        Timber.d(e)
      }
    }
  }

  fun playSound(soundCommend: DroidCommand.PlaySound) {
    viewModelScope.launch {
      try {
        droidOperator.playSound(soundCommend)
      } catch (e: Exception) {
        ensureActive()
        Timber.d(e)
      }
    }
  }
}
