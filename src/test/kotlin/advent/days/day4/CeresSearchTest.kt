package advent.days.day4

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CeresSearchTest {

  @Test
  fun `reads input`() {
    val subject = CeresSearch.fromResource("day4/xmas-matrix.txt")

    assertThat(subject.countXMAS()).isEqualTo(8)
    assertThat(subject.countCrossedMas()).isEqualTo(1)
  }

  @Nested
  inner class `#countXMAS`() {
    @Test
    fun `Returns 0 when no XMAS found`() {
      val subject = CeresSearch.forString("HXIMASHI")

      assertThat(subject.countXMAS()).isEqualTo(0)
    }

    @Test
    fun `Find XMAS horizontally forward`() {
      val subject = CeresSearch.forString("HIXMASNOWXMAS")

      assertThat(subject.countXMAS()).isEqualTo(2)
    }

    @Test
    fun `Find XMAS over multiple lines`() {
      val subject = CeresSearch.forString("HI\nXMAS\nNono\nXMAS")

      assertThat(subject.countXMAS()).isEqualTo(2)
    }

    @Test
    fun `Find XMAS horizontally backward`() {
      val subject = CeresSearch.forString("HISAMXNOWXMAS")

      assertThat(subject.countXMAS()).isEqualTo(2)
    }

    @Test
    fun `Find XMAS vertically`() {
      val subject =
        CeresSearch.forString(
          """
        X
        M
        A
        S
      """
            .trimIndent()
        )

      assertThat(subject.countXMAS()).isEqualTo(1)
    }

    @Test
    fun `Find XMAS vertically backwards`() {
      val subject =
        CeresSearch.forString(
          """
        XSA
        MAAS
        AMAS
        ZXA
      """
            .trimIndent()
        )

      assertThat(subject.countXMAS()).isEqualTo(1)
    }

    @Test
    fun `Find XMAS diagonal downwards`() {
      val subject =
        CeresSearch.forString(
          """
        X...
        .M..
        ..A.
        ...S
      """
            .trimIndent()
        )

      assertThat(subject.countXMAS()).isEqualTo(1)
    }

    @Test
    fun `Find XMAS diagonal downwards backwards`() {
      val subject =
        CeresSearch.forString(
          """
        ...X
        ..M.
        .A..
        S...
      """
            .trimIndent()
        )

      assertThat(subject.countXMAS()).isEqualTo(1)
    }

    @Test
    fun `Find XMAS diagonal upwards`() {
      val subject =
        CeresSearch.forString(
          """
        ...S
        ..A.
        .M..
        X...
      """
            .trimIndent()
        )

      assertThat(subject.countXMAS()).isEqualTo(1)
    }

    @Test
    fun `Find XMAS diagonal upwards backwards`() {
      val subject =
        CeresSearch.forString(
          """
        S...
        .A..
        ..M.
        ...X
      """
            .trimIndent()
        )

      assertThat(subject.countXMAS()).isEqualTo(1)
    }

    @Test
    fun `Find XMAS with overlapping matches`() {
      val subject =
        CeresSearch.forString(
          """
        SAMX
        .AM.
        .AM.
        S..X
      """
            .trimIndent()
        )

      assertThat(subject.countXMAS()).isEqualTo(3)
    }
  }

  @Nested
  inner class `#countCrossedMas`() {
    @Test
    fun `Returns 0 when no Crossed MASes found`() {
      val subject = CeresSearch.forString("HXIMASHI")

      assertThat(subject.countCrossedMas()).isEqualTo(0)
    }

    @Test
    fun `Find Crossed masses`() {
      val subject =
        CeresSearch.forString(
          """
          M.M
          .A.
          S.S
        """
            .trimIndent()
        )

      assertThat(subject.countCrossedMas()).isEqualTo(1)
    }

    @Test
    fun `Find Crossed masses in uneven matrix`() {
      val subject =
        CeresSearch.forString(
          """
          M.M
          .A.A
          S.S.S
        """
            .trimIndent()
        )

      assertThat(subject.countCrossedMas()).isEqualTo(1)
    }
  }
}
