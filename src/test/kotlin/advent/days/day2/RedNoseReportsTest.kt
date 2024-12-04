package advent.days.day2

import advent.days.day2.RedNoseReports.Report
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RedNoseReportsTest {

  @Test
  fun `Counts safe reports`() {
    val subject = RedNoseReports.fromResource("day2/reports-valid.txt")

    assertThat(subject.countSafeReports()).isEqualTo(2)
  }

  @Test
  fun `Counts safe reports - applying dampening`() {
    val subject = RedNoseReports.fromResource("day2/reports-valid.txt")

    assertThat(subject.countSafeReports()).isEqualTo(2)
  }

  @Nested
  inner class Report {
    @Test
    fun `#isSafe returns true when values increase by 1-3`() {
      val subject = Report(listOf(1, 2, 3, 6, 7))

      assertThat(subject.isSafe()).isTrue()
    }

    @Test
    fun `#isSafe returns true when values decreases by 1-3`() {
      val subject = Report(listOf(2, 0, -3))

      assertThat(subject.isSafe()).isTrue()
    }

    @Test
    fun `#isSafe returns false when values increase more than 3`() {
      val subject = Report(listOf(1, 3, 9))

      assertThat(subject.isSafe()).isFalse()
    }

    @Test
    fun `#isSafe returns false when values decreases by more than 3`() {
      val subject = Report(listOf(99, 89))

      assertThat(subject.isSafe()).isFalse()
    }

    @Test
    fun `#isSafe returns false when values are not all increasing or decreasing`() {
      val subject = Report(listOf(0, 2, 4, 2))

      assertThat(subject.isSafe()).isFalse()
    }

    @Test
    fun `#isSafe returns false when values are not either increasing or decreasing`() {
      val subject = Report(listOf(0, 0))

      assertThat(subject.isSafe()).isFalse()
    }

    @Test
    fun `isSafe returns true if unsafe levels are within tolerance`() {
      val subject = Report(listOf(1, 2, 7, 4, 5))

      assertThat(subject.isSafe(unsafeLevelTolerance = 0)).isFalse()
      assertThat(subject.isSafe(unsafeLevelTolerance = 1)).isTrue()
    }

    @Test
    fun `isSafe returns false if unsafe levels fall outside tolerance`() {
      val subject = Report(listOf(9, 7, 6, 2, 1))

      assertThat(subject.isSafe(unsafeLevelTolerance = 1)).isFalse()
      assertThat(subject.isSafe(unsafeLevelTolerance = 2)).isTrue()
    }

    @Test
    fun `isSafe returns true if first level is wrong but is withing tolerance`() {
      val subject = Report(listOf(19, 7, 6, 5, 4))

      assertThat(subject.isSafe(unsafeLevelTolerance = 1)).isTrue()
    }
  }

  @Nested
  inner class InputsToIntegerList {
    @Test
    fun `Returns an empty list when given an empty list`() {
      val reports = RedNoseReports.inputsToIntegerList(emptyList())

      assertThat(reports).isEmpty()
    }

    @Test
    fun `Returns a list of Report objects given a list of strings with integers`() {
      val reports = RedNoseReports.inputsToIntegerList(listOf("1", "11 22", "111 222 333"))

      assertThat(reports)
        .containsExactly(Report(listOf(1)), Report(listOf(11, 22)), Report(listOf(111, 222, 333)))
    }

    @Test
    fun `throws when lists contain non-integers`() {
      assertThrows<NumberFormatException>("A is not a number") {
        RedNoseReports.inputsToIntegerList(listOf("1", "1 A", "1 2 3"))
      }
    }
  }
}
