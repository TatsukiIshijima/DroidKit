package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand

class DroidOperatorImpl(
  private val droidConnector: DroidConnector
) : DroidOperator {

  override suspend fun go(speed: Double) {
    droidConnector.writeValue(DroidCommand.WheelAction.Move.Go(speed))
  }

  override suspend fun back(speed: Double) {
    droidConnector.writeValue(DroidCommand.WheelAction.Move.Back(speed))
  }

  override suspend fun turn(degree: Double) {
    droidConnector.writeValue(DroidCommand.WheelAction.Turn.Steer(degree))
  }

  override suspend fun stop() {
    droidConnector.writeValue(DroidCommand.WheelAction.Move.End)
  }

  override suspend fun endTurn() {
    droidConnector.writeValue(DroidCommand.WheelAction.Turn.End)
  }

  override suspend fun changeLEDColor(red: Int, green: Int, blue: Int) {
    droidConnector.writeValue(DroidCommand.ChangeLEDColor(red, green, blue))
  }

  override suspend fun playSound(soundCommand: DroidCommand.PlaySound) {
    droidConnector.writeValue(soundCommand)
  }
}