package com.tatsuki.droidkit

interface DroidClient {

  suspend fun connect(
    onSuccess: () -> Unit,
    onFailure: (errorCode: Int) -> Unit
  )

  fun disconnect()
}