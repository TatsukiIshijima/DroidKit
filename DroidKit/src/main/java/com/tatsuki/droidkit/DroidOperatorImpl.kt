package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.tatsuki.droidkit.common.DroidBLE
import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkit.model.DroidRawData
import java.util.UUID

class DroidOperatorImpl(
  private val droidConnector: DroidConnector
) : DroidOperator {

  override suspend fun go() {

  }

  override suspend fun back() {

  }

  override suspend fun turn() {

  }

  override suspend fun stop() {

  }

  override suspend fun endTurn() {

  }

  override suspend fun changeLEDColor() {

  }

  override suspend fun playSound(soundCommand: DroidCommand.PlaySound) {
    droidConnector.writeValue(soundCommand)
  }
}