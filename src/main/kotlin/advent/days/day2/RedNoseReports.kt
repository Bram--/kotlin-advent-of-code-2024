package advent.days.day2

import advent.shared.readResourceAsStringList
import kotlin.math.abs

/**
 * [Day 2 Kotlin Advent of Code 2024 - Red Nose Reports.](https://adventofcode.com/2024/day/2)
 *
 * The engineers at the Red-Nosed Reindeer nuclear power plant need help analyzing reports to
 * determine which ones are safe based on specific criteria for increasing or decreasing levels. You
 * need to count how many reports meet these safety criteria.
 *
 * ```
 * ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⡀⣴⢦⡀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡴⠖⢦⡀⠀⠀⣀⡘⣧⠙⠚⠀⡇⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠀⠀⢀⣀⡀⠀⠀⠀⢧⣀⣴⠃⠀⠘⢯⣭⠉⣤⠤⠞⠁⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⣰⢲⣸⠄⢧⡼⣫⠇⢀⡼⠉⢉⣛⣒⣶⣫⠾⢁⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠸⣄⣀⣀⣠⢮⣙⣒⣾⠴⠊⠉⠉⣈⠉⣷⢞⡽⠋⡿⠀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠀⠀⠉⠉⢀⣠⣤⣤⣧⣀⡴⠚⠉⠉⠉⠃⣾⡤⠞⠁⠀⠀⢰⣲⣶⣵⣄⠀
 * ⠀⠀⠀⠀⠀⠈⠻⣍⣙⣒⣮⡍⠀⠀⡀⢀⣀⠀⠈⢧⡀⠀⠀⠀⣞⣿⡏⡿⡷⠁
 * ⢠⡤⣴⣶⠀⠀⠀⠀⠈⠉⢹⠀⠀⠘⠿⠘⠋⠀⠀⠀⢳⡀⠀⠀⠀⠹⠝⠊⠊⠀
 * ⢰⡕⡊⣻⣿⠃⠀⠀⠀⠀⢸⠀⢀⡤⠖⡺⢯⠉⠉⠓⢆⢧⠀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠑⠷⠛⠛⠀⠀⠀⠀⠀⢸⢰⠏⠀⠀⠓⠋⠀⡀⠀⡼⡿⠀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⣯⣧⣄⣈⣒⣒⣩⠴⢚⣵⡇⠀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣦⣏⣳⠻⠿⡷⠲⠛⣋⣸⡏⠀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⢀⣀⣀⡀⣰⠃⠉⠉⢙⡿⡟⠋⣭⠍⢇⢳⡀⢀⣀⣄⠀⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⣾⠀⢻⠉⡏⠀⠀⠀⣏⡼⢉⣠⡗⠒⢹⠈⡏⢉⡏⠈⡇⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⢻⡀⣾⠀⡇⠀⠀⠀⣏⠓⠦⢞⣯⣩⠜⠀⡇⠸⡇⢰⠃⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⠈⠓⠧⠖⢳⡖⠋⠉⢹⡶⠶⣾⠉⠉⠓⢶⠓⠦⠗⠋⠀⠀⠀⠀
 * ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⠒⠒⠛⠁⠀⠘⠓⠒⠒⠋⠀⠀⠀⠀⠀⠀⠀⠀
 * ```
 */
class RedNoseReports private constructor(private val reports: List<Report>) {

  /** Returns the number of safe reports. */
  fun countSafeReports(unsafeLevelTolerance: Int = 0): Int =
    reports.count { report -> report.isSafe(unsafeLevelTolerance) }

  /** Contains the report data/levels needed to */
  internal data class Report(val levels: List<Int>) {
    /**
     * Checks whether a report is safe or not.
     *
     * A report is considered safe if it meets the following prerequisites:
     * * The levels are either all increasing or all decreasing.
     * * Any two adjacent levels differ by at least one and at most three.
     *
     * @param unsafeLevelTolerance Allow for a number of unsafe level transitions.
     */
    fun isSafe(unsafeLevelTolerance: Int = 0, levels: List<Int> = this.levels): Boolean {
      if (unsafeLevelTolerance < 0) return false
      if (levels.size < 2) return true

      val allIncreasing = levels.zipWithNext().all { it.first < it.second }
      val allDecreasing = levels.zipWithNext().all { it.first > it.second }
      val allSafeIncrements = levels.zipWithNext().all { abs(it.first - it.second) in 1..3 }

      if ((allIncreasing || allDecreasing) && allSafeIncrements) return true
      for (i in levels.indices) {
        val newLevels = levels.toMutableList().also { it.removeAt(i) }
        if (isSafe(unsafeLevelTolerance - 1, newLevels)) return true
      }

      return false
    }
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      fromResource("day2/reports.txt").apply {
        println(
          """
          Red Nose Reports: - using `resources/day2/reports.txt
          
          Safe reports: ${countSafeReports()}
          Safe reports with Problem Dampener: ${countSafeReports(unsafeLevelTolerance = 1)}
        """
            .trimIndent()
        )
      }
    }

    /** Returns a [RedNoseReports] from a resource path. */
    fun fromResource(path: String): RedNoseReports =
      readResourceAsStringList(path)!!.let { lines ->
        return RedNoseReports(inputsToIntegerList(lines))
      }

    /**
     * Given a list of strings - in the format below - returns a [List] of [Int] primitives for each
     * line/string.
     *
     * ```
     * 7 6 4 2 1
     * 1 2 7 8 9
     * 9 7 6 2 1
     * 1 3 2 4 5
     * 8 6 4 4 1
     * 1 3 6 7 9
     * ```
     */
    internal fun inputsToIntegerList(stringList: List<String>): List<Report> =
      stringList.map { it.split(Regex("\\s+")) }.map { Report(it.map { level -> level.toInt() }) }
  }
}
