package com.tatsuki.droidkit

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.tatsuki.droidkit.common.DroidBLE
import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkit.model.DroidRawData
import java.util.UUID

class DroidOperatorImpl(
  private val gatt: BluetoothGatt,
) : DroidOperator {

  @SuppressLint("MissingPermission")
  private fun writeValue(command: DroidCommand) {
    val characteristic = gatt
      .getService(UUID.fromString(DroidBLE.W32_SERVICE_UUID))
      .getCharacteristic(UUID.fromString(DroidBLE.W32_CHARACTERISTIC)) ?: return
    val rawData = DroidRawData.create(command)
    characteristic.value = rawData.values
    gatt.writeCharacteristic(characteristic)
  }

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
    writeValue(soundCommand)
  }
}