package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand

interface DroidClient {

  suspend fun connect(
    onSuccess: () -> Unit,
    onFailure: (errorCode: Int) -> Unit
  )

  fun disconnect()

  fun isConnected(): Boolean

  suspend fun playSound(command: DroidCommand.PlaySound)
}