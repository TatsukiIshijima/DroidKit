package com.tatsuki.droidkit.common

// https://github.com/NordicSemiconductor/Android-BLE-Common-Library/blob/c078e2579fe5c0b6d29d70cd9fc9614ac7c0d355/ble-common/src/main/java/no/nordicsemi/android/ble/common/util/CRC16.java#L167
// https://github.com/crane-hiromu/DroidKit/blob/main/Sources/DroidKit/Extension/Array%2BExtensions.swift#L11
fun ByteArray.asCRC16(): Int {
  var bit: Boolean
  var c15: Boolean
  var crc = 65535

  this.forEach {b ->
    for (i in 0 .. 7) {
      bit = (b.toInt() shr (7 - i) and 1) == 1
      c15 = ((crc shr 15) and 1) == 1
      crc = crc shl 1

      if (c15 != bit) {
        crc = crc xor 4129
      }
    }
  }
  return crc and 65535
}