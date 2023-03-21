package com.tatsuki.droidkit

sealed class DroidException : Exception() {

  object NotReadyException : DroidException()

  data class ScanFailedException(
    val errorCode: Int,
  ) : DroidException()
}