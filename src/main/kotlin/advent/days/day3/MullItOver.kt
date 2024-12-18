package advent.days.day3

import advent.shared.readResourceAsStringList

/**
 * [Day 3 Kotlin Advent of Code 2024 - Mull it over.](https://adventofcode.com/2024/day/3)
 *
 * The North Pole Toboggan Rental Shop's computer has corrupted memory with jumbled instructions,
 * including some valid multiplication commands like mul(X, Y). You need to find and execute only
 * the valid multiplication instructions and sum their results to help fix the computer.
 *
 * ```
 *          {_}
 *           / \
 *          /   \
 *         /_____\
 *       {`_______`}
 *        // . . \\
 *       (/(__7__)\)
 *       |'-' = `-'|
 *       |         |
 *       /\       /\
 *      /  '.   .'  \
 *     /_/   `"`   \_\
 *    {__}###[_]###{__}
 *    (_/\_________/\_)
 *        |___|___|
 *   jgs   |--|--|
 *        (__)`(__)
 *
 * ```
 */
class MullItOver private constructor(private val numbers: List<Pair<Int, Int>>) {

  fun sum() = numbers.sumOf { it.first * it.second }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      fromResource("day3/input.txt", ignoreDisabled = false).apply {
        println(
            """
          Mull it over - using `resources/day3/input.txt
          
          Correct memory is summed as: ${sum()}.
        """
                .trimIndent())
      }
    }

    /** Returns a [MullItOver] from a resource path. */
    fun fromResource(path: String, ignoreDisabled: Boolean = true): MullItOver =
        readResourceAsStringList(path)!!.let { lines ->
          return MullItOver(extractMulInstructions(lines.joinToString(), ignoreDisabled))
        }

    private const val MUL_PATTERN = "mul\\((\\d{1,3})[\\s]*,[\\s]*(\\d{1,3})\\)"
    private const val DO_DONT_PATTERN = "(?:do|don't)\\(\\)"

    fun extractMulInstructions(
        instructionString: String,
        ignoreDisabled: Boolean = true
    ): List<Pair<Int, Int>> {
      val instructions = Regex("$MUL_PATTERN|$DO_DONT_PATTERN").findAll(instructionString)
      var lastInstruction = "do()"

      return instructions
          .fold(mutableListOf<Pair<Int, Int>>()) { acc, instruction ->
            val match = instruction.value
            if (match == "do()" || match == "don't()") {
              if (!ignoreDisabled) lastInstruction = instruction.value
            } else if (lastInstruction == "do()") {
              val values = instruction.groupValues.subList(1, 3).map { it.toInt() }
              acc.add(Pair(values[0], values[1]))
            }

            acc
          }
          .toList()
    }
  }
}
