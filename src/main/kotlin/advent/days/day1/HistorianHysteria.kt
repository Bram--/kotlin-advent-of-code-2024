package advent.days.day1

import advent.shared.printFormatted
import advent.shared.readResourceAsStringList
import kotlin.math.abs

/**
 * [Day 1 Kotlin Advent of Code 2024 - Historian Hysteria.](https://adventofcode.com/2024/day/1)
 *
 * The Chief Historian is missing, and to find him, you need to reconcile two lists of historical
 * location IDs by calculating the total distance between corresponding numbers in each list. This
 * will help the Elves determine which locations to search for the Chief Historian before Christmas.
 *
 * ```
 *    .-.                                                   \ /
 *   ( (                                |                  - * -
 *    '-`                              -+-                  / \
 *             \            o          _|_          \
 *             ))          }^{        /___\         ))
 *           .-#-----.     /|\     .---'-'---.    .-#-----.
 *      ___ /_________\   //|\\   /___________\  /_________\
 *     /___\ |[] _ []|    //|\\    | A /^\ A |    |[] _ []| _.O,_
 * ....|"#"|.|  |*|  |...///|\\\...|   |"|   |....|  |*|  |..(^)....
 * ```
 */
class HistorianHysteria
private constructor(private val left: List<Int>, private val right: List<Int>) {

  /** Calculates the total distance between pair of locations in the two lists. */
  fun calculateDistance() = left.zip(right).sumOf { (left, right) -> abs(left - right) }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val historianHysteria = fromResource("day1/historian-hysteria.txt")

      historianHysteria.calculateDistance().printFormatted("Calculated distance: %s")
    }

    /** Returns a [HistorianHysteria] from a resource path. */
    fun fromResource(path: String): HistorianHysteria =
      readResourceAsStringList(path)!!.let { lines ->
        val values = inputsToSortedLists(lines)
        return HistorianHysteria(values.first, values.second)
      }

    /**
     * Given a list of strings - in the format below - returns a [Pair] containing a [List] of all
     * the numbers on the left and another [List] with all the numbers on the right.
     *
     * ```
     * 3   4
     * 4   3
     * 2   5
     * 1   3
     * 3   9
     * 3   3
     * ```
     */
    internal fun inputsToSortedLists(stringList: List<String>): Pair<List<Int>, List<Int>> =
      stringList
        .fold(Pair(mutableListOf<Int>(), mutableListOf<Int>())) { accumulator, value ->
          // Splits the line into two segments assuming the format "\d+[\s]+\d+".
          val parts = value.split(Regex("\\s+"))
          require(parts.size == 2) { "Lists have different lengths" }

          // Adds both values to the appropriate lists.
          val (left, right) = parts.map { it.toInt() }
          accumulator.apply {
            first.add(left)
            second.add(right)
          }
        }
        .let {
          // Sorts the lists and ensures a stable list is returned.
          Pair(it.first.sorted(), it.second.sorted())
        }
  }
}
