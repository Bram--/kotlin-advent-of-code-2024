package advent.days.day1

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HistorianHysteriaTest {
  @Test
  fun `Ensures lists are of the same size`() {
    assertThrows<IllegalArgumentException>("Lists have different lengths") {
      HistorianHysteria.fromResource("day1/historian-hysteria-different-lengths.txt")
    }
  }

  @Test
  fun `calculate Distances`() {
    val subject = HistorianHysteria.fromResource("day1/historian-hysteria-valid.txt")

    assertThat(subject.calculateDistance()).isEqualTo(895)
  }

  @Nested
  inner class InputsToSortedLists {
    @Test
    fun `Returns a list for the left and for the right`() {
      val (leftList, rightList) = HistorianHysteria.inputsToSortedLists(listOf("1  2", "4  5"))

      assertThat(leftList).hasSize(2)
      assertThat(rightList).hasSize(2)
    }

    @Test
    fun `Returns a sorted lists`() {
      val (leftList, rightList) =
        HistorianHysteria.inputsToSortedLists(listOf("1 10", "2 5", "3 3", "5 2"))

      assertThat(leftList).containsExactly(1, 2, 3, 5).inOrder()
      assertThat(rightList).containsExactly(2, 3, 5, 10).inOrder()
    }

    @Test
    fun `Returns an empty list when a empty list is given`() {
      val (leftList, rightList) = HistorianHysteria.inputsToSortedLists(emptyList())

      assertThat(leftList).isEmpty()
      assertThat(rightList).isEmpty()
    }

    @Test
    fun `Throws an exception when a input contains non-numbers`() {
      assertThrows<NumberFormatException>("For input string: \"A\"") {
        HistorianHysteria.inputsToSortedLists(listOf("1 A"))
      }
    }

    @Test
    fun `Throws an exception when list have a different length`() {
      assertThrows<IllegalArgumentException>("Lists have different lengths") {
        HistorianHysteria.inputsToSortedLists(listOf("1 4", "2   "))
      }
    }
  }
}
