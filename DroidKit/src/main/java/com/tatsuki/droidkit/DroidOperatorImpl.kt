package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand

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

  override suspend fun changeLEDColor(red: Int, green: Int, blue: Int) {
    droidConnector.writeValue(DroidCommand.ChangeLEDColor(red, green, blue))
  }

  override suspend fun playSound(soundCommand: DroidCommand.PlaySound) {
    droidConnector.writeValue(soundCommand)
  }
}