package com.tatsuki.droidkit.model

import com.tatsuki.droidkit.common.ext.asCRC16

data class DroidRawData(
  val values: ByteArray,
) {

  companion object {
    fun create(command: DroidCommand): DroidRawData {
      val payload = when (command) {
        // TODO:他のコマンド対応
        is DroidCommand.ChangeLEDColor -> byteArrayOf(command.red.toByte(), command.green.toByte(), command.blue.toByte())
        is DroidCommand.PlaySound -> byteArrayOf(command.type.toByte())
        is DroidCommand.MoveWheel.Go -> byteArrayOf(command.moveType.toByte(), command.value().toByte())
        is DroidCommand.MoveWheel.Back -> byteArrayOf(command.moveType.toByte(), command.value().toByte())
        is DroidCommand.MoveWheel.End -> byteArrayOf(command.moveType.toByte(), command.value().toByte())
      }
      val payloadCount = payload.count()
      val byteArray = ByteArray(payloadCount + 4)
      val crc = payload.asCRC16()
      val byteArrayCount = byteArray.count()

      byteArray[0] = ((command.code shl 1) or ((payloadCount and 256) shr 8)).toByte()
      byteArray[1] = (payloadCount and 255).toByte()

      for (i in 0 until payloadCount) {
        byteArray[i + 2] = payload[i]
      }

      byteArray[byteArrayCount - 1] = (crc and 255).toByte()
      byteArray[byteArrayCount - 2] = ((crc and 65280) shr 8).toByte()

      return DroidRawData(byteArray)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as DroidRawData

    if (!values.contentEquals(other.values)) return false

    return true
  }

  override fun hashCode(): Int {
    return values.contentHashCode()
  }
}
