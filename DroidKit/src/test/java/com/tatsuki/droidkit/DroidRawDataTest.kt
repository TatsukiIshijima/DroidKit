package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkit.model.DroidRawData
import org.junit.Test

class DroidRawDataTest {

  @Test
  fun test_create() {
    val rawData = DroidRawData.create(DroidCommand.PlaySound.S0)

    val expectRawData = listOf(30, 1, 0, 225, 240)
    assert(rawData.values.size == expectRawData.size)

    for ((i, expect) in expectRawData.withIndex()) {
      assert(rawData.values[i] == expect)
    }
  }
}