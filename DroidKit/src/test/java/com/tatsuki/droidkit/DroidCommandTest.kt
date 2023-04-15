package com.tatsuki.droidkit

import com.tatsuki.droidkit.model.DroidCommand
import org.junit.Test

class DroidCommandTest {

  @Test
  fun test_steer() {
    val steer90 = DroidCommand.WheelAction.Turn.Steer(90.0)
    assert(steer90.value() == 128)
  }

  @Test
  fun test_go() {
    val speed05 = DroidCommand.WheelAction.Move.Go(0.5)
    assert(speed05.value() == 127 + 64)
  }

  @Test
  fun test_back() {
    val speed05 = DroidCommand.WheelAction.Move.Back(0.5)
    assert(speed05.value() == 128 - 64)
  }
}