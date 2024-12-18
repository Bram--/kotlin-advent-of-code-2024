package advent.days.day3

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MullItOverTest {

  @Test
  fun `Returns the sum of all mul instructions ignoring disabled`() {
    val subject = MullItOver.fromResource("day3/instructions-valid.txt")

    assertThat(subject.sum()).isEqualTo(431)
  }

  @Test
  fun `Returns the sum of all mul instructions not ignoring disabled`() {
    val subject = MullItOver.fromResource("day3/instructions-valid.txt", ignoreDisabled = false)

    assertThat(subject.sum()).isEqualTo(231)
  }

  @Nested
  inner class ExtractMulInstructions {
    @Test
    fun `extract simple mul instructions`() {
      val mulInstructions = MullItOver.extractMulInstructions("mul(2,3);mul(90, 80)")

      assertThat(mulInstructions).containsExactly(Pair(2, 3), Pair(90, 80))
    }

    @Test
    fun `extract mul instructions with corrupted memory`() {
      val mulInstructions = MullItOver.extractMulInstructions("mul(2,3);do_not_mul(45,67)")

      assertThat(mulInstructions).containsExactly(Pair(2, 3), Pair(45, 67))
    }

    @Test
    fun `extract mul instructions ignores non-number characters`() {
      val mulInstructions = MullItOver.extractMulInstructions("mul(2,ab);do_not_mul(45,67)")

      assertThat(mulInstructions).containsExactly(Pair(45, 67))
    }

    @Test
    fun `returns empty list if no mul instructions`() {
      val mulInstructions = MullItOver.extractMulInstructions("Hello")

      assertThat(mulInstructions).isEmpty()
    }

    @Test
    fun `ignores numbers in the thousands`() {
      val mulInstructions = MullItOver.extractMulInstructions("mul(9,9);mul(1000,99)")

      assertThat(mulInstructions).containsExactly(Pair(9, 9))
    }
  }

  @Nested
  inner class ExtractMulInstructionsIgnoringDisabled {
    @Test
    fun `extract simple mul instructions`() {
      val mulInstructions =
        MullItOver.extractMulInstructions("mul(2,3);mul(90, 80)", ignoreDisabled = false)

      assertThat(mulInstructions).containsExactly(Pair(2, 3), Pair(90, 80))
    }

    @Test
    fun `extract mul instructions with corrupted memory`() {
      val mulInstructions =
        MullItOver.extractMulInstructions("mul(2,3);do_not_mul(45,67)", ignoreDisabled = false)

      assertThat(mulInstructions).containsExactly(Pair(2, 3), Pair(45, 67))
    }

    @Test
    fun `extract mul instructions ignores non-number characters`() {
      val mulInstructions =
        MullItOver.extractMulInstructions("mul(2,ab);do_not_mul(45,67)", ignoreDisabled = false)

      assertThat(mulInstructions).containsExactly(Pair(45, 67))
    }

    @Test
    fun `returns empty list if no mul instructions`() {
      val mulInstructions = MullItOver.extractMulInstructions("Hello", ignoreDisabled = false)

      assertThat(mulInstructions).isEmpty()
    }

    @Test
    fun `ignores numbers in the thousands`() {
      val mulInstructions =
        MullItOver.extractMulInstructions("mul(9,9);mul(1000,99)", ignoreDisabled = false)

      assertThat(mulInstructions).containsExactly(Pair(9, 9))
    }

    @Test
    fun `ignores numbers that a preceded by don't`() {
      val mulInstructions =
        MullItOver.extractMulInstructions(
          "mul(9,9);don't();mul(100,99)mul(2,2)",
          ignoreDisabled = false,
        )

      assertThat(mulInstructions).containsExactly(Pair(9, 9))
    }

    @Test
    fun `reenables summing after a do instruction`() {
      val mulInstructions =
        MullItOver.extractMulInstructions(
          "mul(9,9);don't();mul(100,99);do();mul(2,2)",
          ignoreDisabled = false,
        )

      assertThat(mulInstructions).containsExactly(Pair(9, 9), Pair(2, 2))
    }

    @Test
    fun `ignores dont instruction`() {
      val mulInstructions =
        MullItOver.extractMulInstructions("mul(9,9);dont();mul(100,99)", ignoreDisabled = false)

      assertThat(mulInstructions).containsExactly(Pair(9, 9), Pair(100, 99))
    }
  }
}
