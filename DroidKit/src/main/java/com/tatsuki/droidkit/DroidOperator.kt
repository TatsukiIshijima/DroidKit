package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand

interface DroidOperator {
  suspend fun go(speed: Double)
  suspend fun back(speed: Double)
  suspend fun turn()
  suspend fun stop()
  suspend fun endTurn()
  suspend fun changeLEDColor(red: Int, green: Int, blue: Int)
  suspend fun playSound(soundCommand: DroidCommand.PlaySound)
}