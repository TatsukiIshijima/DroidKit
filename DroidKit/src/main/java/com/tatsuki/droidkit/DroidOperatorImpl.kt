package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand

class DroidOperatorImpl(
  private val droidConnector: DroidConnector
) : DroidOperator {

  override suspend fun go(speed: Double) {
    droidConnector.writeValue(DroidCommand.MoveWheel.Go(speed))
  }

  override suspend fun back(speed: Double) {
    droidConnector.writeValue(DroidCommand.MoveWheel.Back(speed))
  }

  override suspend fun turn() {

  }

  override suspend fun stop() {
    droidConnector.writeValue(DroidCommand.MoveWheel.End())
  }

  override suspend fun endTurn() {

  }

  override suspend fun changeLEDColor(red: Int, green: Int, blue: Int) {
    droidConnector.writeValue(DroidCommand.ChangeLEDColor(red, green, blue))
  }

  override suspend fun playSound(soundCommand: DroidCommand.PlaySound) {
    droidConnector.writeValue(soundCommand)
  }
}