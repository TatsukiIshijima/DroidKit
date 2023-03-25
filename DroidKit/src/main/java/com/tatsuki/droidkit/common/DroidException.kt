package com.tatsuki.droidkit.common

sealed class DroidException : Exception() {

  object NotReadyException : DroidException()

  data class ScanFailedException(
    val errorCode: Int,
  ) : DroidException()
}