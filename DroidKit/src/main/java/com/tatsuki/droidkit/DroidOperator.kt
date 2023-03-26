package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand

interface DroidOperator {
  suspend fun go()
  suspend fun back()
  suspend fun turn()
  suspend fun stop()
  suspend fun endTurn()
  suspend fun changeLEDColor()
  suspend fun playSound(soundCommand: DroidCommand.PlaySound)
}