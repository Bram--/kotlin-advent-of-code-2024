package advent.days.day4

import advent.shared.countIndexed
import advent.shared.dropFirst
import advent.shared.readResourceAsStringList
import advent.shared.sumIndexed

/**
 * [Day 4 Kotlin Advent of Code 2024 - Ceres Search.](https://adventofcode.com/2024/day/4) You've
 * been transported to the Ceres monitoring station and tasked with helping an Elf find all
 * instances of the word "XMAS" in a word search puzzle. The word can appear horizontally,
 * vertically, diagonally, backwards, and even overlapping.
 */
class CeresSearch private constructor(private val wordSearch: List<List<String>>) {

  /**
   * Counts the number of times the sequence "XMAS" appears in the word search grid.
   *
   * @return The total count of "XMAS" occurrences.
   */
  fun countXMAS() =
    wordSearch.sumIndexed { rowIndex, row ->
      row.sumIndexed { columnIndex, _ ->
        Direction.entries.count {
          findSequence(wordSearch, Pointer(rowIndex, columnIndex), XMAS_SEQUENCE, it)
        }
      }
    }

  /**
   * Counts the number of "crossed MAS" patterns in the word search grid. A "crossed MAS" is formed
   * when two instances of "MAS" intersect at their middle "A", forming a cross-like pattern.
   *
   * @return The total count of "crossed MAS" patterns.
   */
  fun countCrossedMas(): Int =
    wordSearch.subList(1, wordSearch.size).sumIndexed { rowIndex, row ->
      row.subList(1, row.size).countIndexed { columnIndex, _ ->
        // Only scan if the current position is an "A"
        if (wordSearch[rowIndex + 1][columnIndex + 1] != "A") return@countIndexed false
        if ((rowIndex + 3) > wordSearch.size) return@countIndexed false

        val subMatrix = subMatrix(wordSearch, rowIndex, columnIndex, size = 3)
        (findSequence(subMatrix, Pointer(0, 0), MAS_SEQUENCE, Direction.SOUTHEAST) ||
          findSequence(subMatrix, Pointer(2, 2), MAS_SEQUENCE, Direction.NORTHWEST)) &&
          (findSequence(subMatrix, Pointer(0, 2), MAS_SEQUENCE, Direction.SOUTHWEST) ||
            findSequence(subMatrix, Pointer(2, 0), MAS_SEQUENCE, Direction.NORTHEAST))
      }
    }

  /**
   * Extracts a sub-matrix of the specified size from the given matrix, starting at the given row
   * and column indices. Makes sure the matrix is regular(Not ragged).
   *
   * @param matrix The original matrix.
   * @param rowIndex The starting row index for the sub-matrix.
   * @param columnIndex The starting column index for the sub-matrix.
   * @param size The size (width and height) of the sub-matrix.
   * @return The extracted sub-matrix.
   */
  private fun subMatrix(
    matrix: List<List<String>>,
    rowIndex: Int,
    columnIndex: Int,
    size: Int,
  ): List<List<String>> =
    matrix.subList(rowIndex, rowIndex + size).map { column ->
      MutableList(size) { "." }
        .mapIndexed { index, el -> column.getOrElse(columnIndex + index) { el } }
    }

  /**
   * Recursively searches for a specific sequence of strings in the matrix, starting from the given
   * position and moving in the specified direction.
   *
   * @param matrix The matrix to search within.
   * @param position The starting position for the search.
   * @param sequenceToFind The sequence of strings to search for.
   * @param direction The direction to move in while searching.
   * @return true if the sequence is found, false otherwise.
   */
  private tailrec fun findSequence(
    matrix: List<List<String>>,
    position: Pointer,
    sequenceToFind: List<String>,
    direction: Direction,
  ): Boolean {
    // If there is nothing left in the sequence we've successfully found everything.
    if (sequenceToFind.isEmpty()) return true
    // Out of bounds; impossible to read the position.
    if (!matrix.isInBounds(position)) return false
    // If the value isn't what we're expecting return false.
    if (matrix[position] != sequenceToFind.first()) return false

    // Evaluate the next position for the given direction.
    return findSequence(
      matrix,
      position.nextForDirection(direction),
      sequenceToFind.dropFirst(),
      direction,
    )
  }

  /**
   * Represents a position (row and column) within the word search grid.
   *
   * @property row The row index.
   * @property column The column index.
   */
  private data class Pointer(val row: Int, val column: Int) {
    /**
     * Calculates the next position based on the given direction.
     *
     * @param direction The direction to move in.
     * @return The new position after moving in the specified direction.
     */
    fun nextForDirection(direction: Direction) =
      when (direction) {
        Direction.NORTHWEST -> copy(row = row - 1, column = column - 1)
        Direction.NORTH -> copy(row = row - 1, column = column)
        Direction.NORTHEAST -> copy(row = row - 1, column = column + 1)
        Direction.EAST -> copy(row = row, column = column + 1)
        Direction.SOUTHEAST -> copy(row = row + 1, column = column + 1)
        Direction.SOUTH -> copy(row = row + 1, column = column)
        Direction.SOUTHWEST -> copy(row = row + 1, column = column - 1)
        Direction.WEST -> copy(row = row, column = column - 1)
      }
  }

  /** Represents the possible directions to move within the word search grid. */
  private enum class Direction {
    NORTHWEST,
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
  }

  /**
   * Checks if the given position is within the bounds of the matrix.
   *
   * @param position The position to check.
   * @return True if the position is within bounds, false otherwise.
   */
  private fun List<List<Any>>.isInBounds(position: Pointer) =
    position.row in indices && position.column in this[position.row].indices

  /**
   * Retrieves the element at the specified position in the matrix.
   *
   * @param position The position of the element.
   * @return The element at the given position.
   */
  private operator fun List<List<Any>>.get(position: Pointer) = this[position.row][position.column]

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val search = fromResource("day4/input.txt")

      println(
        """
       XMASes found: ${search.countXMAS()} 
       Crossed MASes found: ${search.countCrossedMas()} 
      """
          .trimIndent()
      )
    }

    val XMAS_SEQUENCE = listOf("X", "M", "A", "S")
    val MAS_SEQUENCE = listOf("M", "A", "S")

    fun forString(wordSearch: String) = CeresSearch(wordSearch.split("\n").map { it.split("") })

    fun fromResource(path: String): CeresSearch =
      readResourceAsStringList(path)!!.let { lines ->
        return forString(lines.joinToString("\n"))
      }
  }
}
