package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkit.model.DroidRawData
import org.junit.Test

class DroidRawDataTest {

  @Test
  fun test_create() {
    val rawData = DroidRawData.create(DroidCommand.PlaySound.S0)

    val expectRawData = byteArrayOf(30, 1, 0, -31, -16)
    assert(rawData.values.size == expectRawData.size)

    for ((i, expect) in expectRawData.withIndex()) {
      assert(rawData.values[i] == expect)
    }
  }
}