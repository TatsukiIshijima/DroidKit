package com.tatsuki.droidkit.model

import kotlin.math.round

sealed interface DroidCommand {
  val code: Int

  data class ChangeLEDColor(
    val red: Int,
    val green: Int,
    val blue: Int,
  ) : DroidCommand {
    override val code: Int
      get() = 9
  }

  sealed interface MoveWheel : DroidCommand {
    override val code: Int
      get() = 10

    val moveType: Int

    fun value(): Int {
      when (this) {
        is Go -> {
          if (speed < 0) return 0
          val result = if (speed in 0.0..1.0) speed else 1.0
          return (127 + round(128.0 * result)).toInt()
        }
        is Back -> {
          if (speed < 0) return 0
          val result = if (speed in 0.0..1.0) speed else 1.0
          return (128 - round(128 * result)).toInt()
        }
        is End -> {
          return 128
        }
      }
    }

    data class Go(
      val speed: Double,
      override val moveType: Int = 2
    ) : MoveWheel

    data class Back(
      val speed: Double,
      override val moveType: Int = 2
    ) : MoveWheel

    data class End(
      override val moveType: Int = 2
    ) : MoveWheel
  }

  sealed class PlaySound(val type: Int) : DroidCommand {
    override val code: Int
      get() = 15

    object S0 : PlaySound(0)
    object S1 : PlaySound(1)
    object S2 : PlaySound(2)
    object S3 : PlaySound(3)
    object S4 : PlaySound(4)
    object S5 : PlaySound(5)
    object S6 : PlaySound(6)
    object S7 : PlaySound(7)
    object S8 : PlaySound(8)
    object S9 : PlaySound(9)
    object S10 : PlaySound(10)
    object S11 : PlaySound(11)
    object S12 : PlaySound(12)
    object S13 : PlaySound(13)
    object S14 : PlaySound(14)
    object S15 : PlaySound(15)
    object S16 : PlaySound(16)
    object S17 : PlaySound(17)
    object S18 : PlaySound(18)
    object S19 : PlaySound(19)
    object S20 : PlaySound(20)
    object S21 : PlaySound(21)
  }
}