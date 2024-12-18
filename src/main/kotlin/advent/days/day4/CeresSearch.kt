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

  fun countXMAS() =
      wordSearch.sumIndexed { rowIndex, row ->
        row.sumIndexed { columnIndex, _ ->
          Direction.entries.count {
            findSequence(wordSearch, Pointer(rowIndex, columnIndex), XMAS_SEQUENCE, it)
          }
        }
      }

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

  private tailrec fun findSequence(
      matrix: List<List<String>>,
      position: Pointer,
      sequenceToFind: List<String>,
      direction: Direction
  ): Boolean {
    // If there is nothing left in the sequence we've successfully found everything.
    if (sequenceToFind.isEmpty()) return true
    // Out of bounds; impossible to read the position.
    if (!matrix.isInBounds(position)) return false
    // If the value isn't what we're expecting return false.
    if (matrix[position] != sequenceToFind.first()) return false

    // Evaluate the next position for the given direction.
    return findSequence(
        matrix, position.nextForDirection(direction), sequenceToFind.dropFirst(), direction)
  }

  private data class Pointer(val row: Int, val column: Int) {
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

  private enum class Direction {
    NORTHWEST,
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST
  }

  private fun List<List<Any>>.isInBounds(position: Pointer) =
      position.row in indices && position.column in this[position.row].indices

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
              .trimIndent())
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
