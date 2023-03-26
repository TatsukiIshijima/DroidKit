package com.tatsuki.droidkit

import com.tatsuki.droidkit.common.asCRC16
import org.junit.Test

class ByteArrayExtTest {

  @Test
  fun test_asCRC16() {
    val payload = byteArrayOf(0.toByte())
    val crc = payload.asCRC16()
    print(crc)
    assert(crc == 57840)
  }
}