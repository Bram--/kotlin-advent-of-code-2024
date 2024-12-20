package advent.days.day5

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PrintQueueTest {

  @Test
  fun `reads from resource`() {
    val subject = PrintQueue.fromResource("day5/valid.txt")

    assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(2)
    assertThat(subject.reorderAndSumIncorrectlyOrderedUpdates()).isEqualTo(9)
  }

  @Nested
  inner class `#sumCorrectlyOrderedUpdates()` {
    @Test
    fun `counts everything as correctly ordered without rules`() {
      val subject = PrintQueue.fromString(listOf("0,1,0", "1,1,1", "2,1,2"))

      // 1 + 1 + 1
      assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(3)
    }

    @Test
    fun `counts everything as correctly ordered with adhered rules`() {
      val subject = PrintQueue.fromString(listOf("1|3", "1,2,3", "1,3,3", "1,2,2"))

      // 2 + 3 + 2
      assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(7)
    }

    @Test
    fun `does not counts incorrectly ordered updates`() {
      val subject = PrintQueue.fromString(listOf("1|3", "3,1,1", "1,1,3", "1,2,3,1,2"))

      // 1 + nothing
      assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(1)
    }

    @Test
    fun `respects several rules`() {
      val subject =
        PrintQueue.fromString(
          listOf("1|3", "2|3", "3|4", "1,2,3,4,5", "4,3,1", "1,2,4,3,3", "1,2,3")
        )

      // 3 + 2
      assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(5)
    }

    @Test
    fun `ignores all if non are correctly ordered`() {
      val subject = PrintQueue.fromString(listOf("1|3", "2|3", "3|4", "4,3,2,1,0", "3,2,1"))

      // 3 + 2
      assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(0)
    }

    @Test
    fun `ignores empty lines`() {
      val subject = PrintQueue.fromString(listOf("1|3", "2|3", "3|4", "", "1,2,3,4,5", ""))

      assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(3)
    }

    @Test
    fun `ignores invalid lines`() {
      val subject = PrintQueue.fromString(listOf("1||3", ",,,,", "0000"))

      assertThat(subject.sumCorrectlyOrderedUpdates()).isEqualTo(0)
    }
  }

  @Nested
  inner class `#reorderAndSumIncorrectlyOrderedUpdates()` {
    @Test
    fun `reorders updates for single rule`() {
      val subject = PrintQueue.fromString(listOf("1|3", "1,2,3", "3,2,1"))

      // [3, 2, 1] => [1, 2, 3] = 2
      assertThat(subject.reorderAndSumIncorrectlyOrderedUpdates()).isEqualTo(2)
    }

    @Test
    fun `reorders multiple updates for single rule`() {
      val subject = PrintQueue.fromString(listOf("1|3", "1,2,3", "3,2,1", "3,4,1"))

      // [3, 2, 1] => [1, 2, 3] = 2
      // [3, 4, 1] => [1, 4, 3] = 4
      assertThat(subject.reorderAndSumIncorrectlyOrderedUpdates()).isEqualTo(6)
    }

    @Test
    fun `reorders updates for multiple rules`() {
      val subject = PrintQueue.fromString(listOf("1|3", "2|3", "1,2,3", "2,3,1"))

      // [2, 3, 1] => [1, 2, 3] = 2
      assertThat(subject.reorderAndSumIncorrectlyOrderedUpdates()).isEqualTo(2)
    }

    @Test
    fun `reorders multiple updates for multiple rules`() {
      val subject = PrintQueue.fromString(listOf("1|3", "3|4", "1,2,3", "3,2,1", "3,4,1"))

      // [3, 2, 1] => [1, 2, 3] = 2
      // [3, 4, 1] => [1, 3, 4] = 3
      assertThat(subject.reorderAndSumIncorrectlyOrderedUpdates()).isEqualTo(5)
    }
  }

  @Test
  fun `reorders multiple updates for multiple rules containing all numbers used`() {
    val subject =
      PrintQueue.fromString(
        """
          1|2
          1|3
          1|4
          1|5
          2|3
          2|4
          2|5
          3|4
          3|5
          4|5
          
          3,2,1,4,5
          5,4,3,2,1
          1,5,2,3,4
          2,5,3,4,1
        """
          .trimIndent()
          .split("\n")
      )

    assertThat(subject.reorderAndSumIncorrectlyOrderedUpdates()).isEqualTo(12)
  }
}
