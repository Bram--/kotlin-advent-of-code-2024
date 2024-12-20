package advent.days.day5

import advent.shared.readResourceAsStringList
import java.util.Collections

/**
 * [Day 5 Kotlin Advent of Code 2024 - Print Queue.](https://adventofcode.com/2024/day/5) The North
 * Pole printing department needs help organizing safety manual updates according to specific page
 * ordering rules. You need to identify the correctly ordered updates and sum their middle page
 * numbers. Then, you need to reorder the incorrect updates and sum their middle page numbers.
 */
class PrintQueue
private constructor(private val rules: Map<Int, List<Int>>, private val updates: List<List<Int>>) {

  /**
   * Calculates the sum of the middle page numbers from the updates that are already correctly
   * ordered according to the rules.
   *
   * @return The sum of the middle page numbers from correctly ordered updates.
   */
  fun sumCorrectlyOrderedUpdates(): Int =
    updates
      .filterNot { update -> update.indices.any { isIncorrectlyOrdered(update, it) } }
      .sumOf { update -> if (update.isNotEmpty()) update[update.size / 2] else 0 }

  /**
   * Reorders the updates that are incorrectly ordered based on the rules, and then calculates the
   * sum of the middle page numbers from the reordered updates.
   *
   * @return The sum of the middle page numbers from the reordered updates.
   */
  fun reorderAndSumIncorrectlyOrderedUpdates(): Int =
    updates
      .asSequence()
      .filter { update -> update.indices.any { isIncorrectlyOrdered(update, it) } }
      .map { update -> update.toMutableList().apply { reOrder(this, 0, this.size - 1) } }
      .sumOf { update -> if (update.isNotEmpty()) update[update.size / 2] else 0 }

  /**
   * Recursively reorders a list of integers based on a set of rules.
   *
   * The rules define which numbers must be followed by other numbers. This function iterates
   * through the list, comparing each number to the numbers that come after it. If a violation of
   * the rules is found (i.e., a number appears before a number it should follow), the numbers are
   * swapped. The reordering process then restarts from the beginning of the list to ensure all
   * rules are satisfied.
   *
   * @param reordered The list of integers to reorder.
   * @param index The current index being considered in the list.
   * @param otherIndex The index of the number being compared to the number at the `index`.
   * @return The reordered list of integers.
   */
  private tailrec fun reOrder(reordered: List<Int>, index: Int, otherIndex: Int): List<Int> {
    if (index >= reordered.size) return reordered

    val numberRules = rules[reordered[otherIndex]] ?: emptyList()
    if (index > otherIndex && numberRules.contains(reordered[index])) {
      // Swap places if the other number contains a "Should be followed by" rule for the current
      // number.
      Collections.swap(reordered, index, otherIndex)

      return reOrder(reordered, 0, reordered.size - 1)
    } else if (otherIndex == 0) {
      // Look at the next element once we've compared the full sequence to itself.
      return reOrder(reordered, index + 1, reordered.size - 1)
    }

    // Look up the rules for the next element (§
    return reOrder(reordered, index, otherIndex - 1)
  }

  /**
   * Checks if a page number at a given index in an update is incorrectly ordered according to the
   * rules.
   *
   * @param update The update list containing the page number.
   * @param index The index of the page number to check.
   * @return True if the page number is incorrectly ordered, false otherwise.
   */
  private fun isIncorrectlyOrdered(update: List<Int>, index: Int): Boolean {
    val pageNumber = update[index]
    val pageNumberRules: List<Int> = rules[pageNumber] ?: emptyList()

    return update.subList(0, index + 1).any { pageNumberRules.contains(it) }
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {

      val printQueue = fromResource("day5/input.txt")

      println(
        """
           Correctly Ordered updates summed: ${printQueue.sumCorrectlyOrderedUpdates()}
           InCorrectly Ordered updates re-ordered and summed: ${printQueue.reorderAndSumIncorrectlyOrderedUpdates()}
        """
          .trimIndent()
      )
    }

    fun fromResource(path: String): PrintQueue = fromString(readResourceAsStringList(path)!!)

    fun fromString(lines: List<String>): PrintQueue {
      val rules = mutableMapOf<Int, MutableList<Int>>()
      val updates = mutableListOf(listOf<Int>())

      lines.forEach { line ->
        val lineRules =
          line.split("|").filter { it.matches(Regex("\\A\\d+\\Z")) }.map { it.toInt() }
        if (lineRules.size == 2) {
          rules.getOrPut(lineRules[0]) { mutableListOf() }.add(lineRules[1])
        }

        val lineUpdates =
          line.split(",").filter { it.matches(Regex("\\A\\d+\\Z")) }.map { it.toInt() }
        updates.add(lineUpdates)
      }

      return PrintQueue(rules, updates)
    }
  }
}
