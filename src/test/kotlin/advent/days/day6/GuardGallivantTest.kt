package advent.days.day6

import advent.days.day6.GuardGallivant.Position
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GuardGallivantTest {

  @Nested
  inner class `#predictPath`() {
    @Test
    fun `Returns when moved off the map`() {
      val subject = GuardGallivant.fromString(listOf("."))

      assertThat(subject.predictPath()).containsExactly(Position(0, 0))
    }

    @Test
    fun `Follows a straight path if no obstacles are encountered`() {
      val subject = GuardGallivant.fromString(listOf(".", ".", "^"))

      assertThat(subject.predictPath())
        .containsExactly(Position(0, 2), Position(0, 1), Position(0, 0))
    }

    @Test
    fun `Does not move into occupied spaces`() {
      val subject = GuardGallivant.fromString(listOf("#", ".", "^"))

      assertThat(subject.predictPath()).containsExactly(Position(0, 2), Position(0, 1))
    }

    @Test
    fun `Complex movement`() {
      val subject =
        GuardGallivant.fromString(
          """
          .#.
          #.#
          .^.
          .#.
        """
            .trimIndent()
            .split("\n")
        )

      assertThat(subject.predictPath())
        .containsExactly(Position(1, 2), Position(1, 1), Position(0, 2))
    }
  }

  @Nested
  inner class `#findCyclicalPaths`() {
    @Test
    fun `Returns when moved off the map`() {
      val subject = GuardGallivant.fromString(listOf("."))

      assertThat(subject.findCyclicalPaths()).isEmpty()
    }

    @Test
    fun `Finds all cyclical paths`() {
      val subject =
        GuardGallivant.fromString(
          """
        .#.
        #.#
        #^#
        ...
      """
            .trimIndent()
            .split("\n")
        )

      assertThat(subject.findCyclicalPaths())
        .containsExactly(Position(x = 1, y = 3), Position(x = 1, y = 2))
    }
  }
}
