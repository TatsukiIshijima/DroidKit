package com.tatsuki.droidkitapp

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tatsuki.droidkit.*
import com.tatsuki.droidkit.common.DroidBLE
import com.tatsuki.droidkit.model.DroidCommand
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.round

class MainViewModel(
  application: Application,
) : AndroidViewModel(application) {

  private val bluetoothAdapter: BluetoothAdapter =
    (application.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
  private val droidScanner: DroidScanner = DroidScannerImpl(bluetoothAdapter)
  private val droidConnector: DroidConnector = DroidConnectorImpl(application)
  private val droidOperator: DroidOperator = DroidOperatorImpl(droidConnector)

  private val mutableSpeedStateFlow = MutableStateFlow(0f)
  val speedStateFlow = mutableSpeedStateFlow.asStateFlow()

  private val mutableDegreeStateFlow = MutableStateFlow(90f)
  val degreeStateFlow = mutableDegreeStateFlow.asStateFlow()

  private val mutableSelectedSoundStateFlow = MutableStateFlow(0)
  val selectedSoundStateFlow = mutableSelectedSoundStateFlow.asStateFlow()

  fun connect() {
    viewModelScope.launch {
      try {
        if (!DroidBLE.isReadyBle(getApplication(), bluetoothAdapter)) {
          throw IllegalStateException()
        }
        droidScanner.startScan()
        val droidDevice = droidScanner.device ?: return@launch
        droidConnector.connect(droidDevice)
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
        droidScanner.stopScan()
        droidConnector.disconnect()
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

  fun onChangeSpeed(speed: Float) {
    mutableSpeedStateFlow.value = speed
  }

  fun go() {
    viewModelScope.launch {
      try {
        droidOperator.go(mutableSpeedStateFlow.value.toDouble())
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun back() {
    viewModelScope.launch {
      try {
        droidOperator.back(mutableSpeedStateFlow.value.toDouble())
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun stop() {
    viewModelScope.launch {
      try {
        mutableSpeedStateFlow.value = 0f
        droidOperator.stop()
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun onChangeDegree(degree: Float) {
    mutableDegreeStateFlow.value = degree
  }

  fun turn() {
    viewModelScope.launch {
      try {
        droidOperator.turn(degreeStateFlow.value.toDouble())
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun reset() {
    viewModelScope.launch {
      try {
        mutableDegreeStateFlow.value = 90f
        droidOperator.endTurn()
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun changeLEDColor(color: Color) {
    viewModelScope.launch {
      try {
        droidOperator.changeLEDColor(
          red = color.red.toUInt8(),
          green = color.green.toUInt8(),
          blue = color.blue.toUInt8(),
        )
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun onChangeSound(type: Int) {
    mutableSelectedSoundStateFlow.value = type
  }

  fun playSound() {
    viewModelScope.launch {
      try {
        val soundCommand = mutableSelectedSoundStateFlow.value.toPlaySoundCommand()
        droidOperator.playSound(soundCommand)
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }
}

fun Int.toPlaySoundCommand(): DroidCommand.PlaySound {
  return DroidCommand.PlaySound::class.sealedSubclasses
    .mapNotNull { it.objectInstance }
    .first { it.type == this }
}

fun Float.toUInt8(): Int {
  return round(255 - 255 * (1f - this)).toInt()
}
