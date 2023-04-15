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
import kotlinx.coroutines.withTimeout
import timber.log.Timber

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

  private val mutableDegreeStateFlow = MutableStateFlow(0f)
  val degreeStateFlow = mutableDegreeStateFlow.asStateFlow()

  private val mutableSelectedSoundStateFlow = MutableStateFlow(0)
  val selectedSoundStateFlow = mutableSelectedSoundStateFlow.asStateFlow()

  fun connect(
    timeoutMill: Long = 5000L,
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

  fun onChangeSpeed(speed: Float) {
    mutableSpeedStateFlow.value = speed
  }

  fun go() {
    viewModelScope.launch {
      try {
        Timber.d("go: speed=${mutableSpeedStateFlow.value}")
//        droidOperator.go(mutableSpeedStateFlow.value)
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun back() {
    viewModelScope.launch {
      try {
        Timber.d("back: speed=${mutableSpeedStateFlow.value}")
//        droidOperator.back(mutableSpeedStateFlow.value)
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
        Timber.d("turn degree=${degreeStateFlow.value}")
//        droidOperator.turn(degreeStateFlow.value)
      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun reset() {
    viewModelScope.launch {
      try {
        Timber.d("rest degree=${degreeStateFlow.value}")

      } catch (e: Exception) {
        ensureActive()
        Timber.e(e)
      }
    }
  }

  fun changeLEDColor(color: Color) {
    viewModelScope.launch {
      try {
        Timber.d("changeLEDColor: selectedColor=$color")
//        droidOperator.changeLEDColor(red, green, blue)
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
        Timber.d("Sound: onClickPositiveButton soundCommand=${soundCommand}")
//        droidOperator.playSound(soundCommand)
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
