package advent.days.day6

import advent.shared.readResourceAsStringList

/** [Day 6 Kotlin Advent of Code 2024 - Guard Gallivant.](https://adventofcode.com/2024/day/6) */
class GuardGallivant
private constructor(private val initialGuardState: Guard, private val initialMap: List<List<Int>>) {

  /**
   * Predicts the path the guard will take through the map.
   *
   * @return A set of [Position] objects representing the unique positions the guard will visit.
   */
  fun predictPath(): Set<Position> {
    return path(initialGuardState).visitedPositions.toSet()
  }

  /**
   * Finds all positions on the map where placing an obstruction will cause the guard to move in a
   * cycle.
   *
   * @return A list of [Position] objects representing the positions where an obstruction will
   *   create a cycle.
   */
  fun findCyclicalPaths(): List<Position> {
    val guardsPath = path(initialGuardState).visitedPositions.toSet()

    return obstructPath(guardsPath.toList(), mutableListOf())
  }

  /**
   * Recursively checks each position in the guard's path to see if obstructing it creates a cycle.
   *
   * @param path The remaining path of the guard to check.
   * @param successfulObstructionPositions A mutable list to store the positions that create cycles.
   * @return A list of [Position] objects representing the positions where an obstruction will
   *   create a cycle.
   */
  private tailrec fun obstructPath(
    path: List<Position>,
    successfulObstructionPositions: MutableList<Position>,
  ): List<Position> {
    if (path.isEmpty()) return successfulObstructionPositions

    return obstructPath(
      path.dropLast(1),
      successfulObstructionPositions.apply {
        val obstructPosition = path.last()

        if (isCyclical(initialMap, path.last(), initialGuardState, mutableListOf())) {
          add(obstructPosition)
        }
      },
    )
  }

  /**
   * Recursively checks if the given path is cyclical/loops the guard.
   *
   * @param map The map the guard is traversing - including obstructions.
   * @param alsoBlock The position to consider as an obstruction.
   * @param currentGuardState The current state of the guard.
   * @param visited A mutable list to store the visited positions and directions to detect cycles.
   * @return True if the guard's path becomes cyclical, false otherwise.
   */
  private tailrec fun isCyclical(
    map: List<List<Int>>,
    alsoBlock: Position,
    currentGuardState: Guard,
    visited: MutableList<PositionWithDirection>,
  ): Boolean {
    if (!map.isInBounds(currentGuardState.position)) return false
    if (
      visited.contains(
        PositionWithDirection(currentGuardState.position, currentGuardState.directionInDegrees)
      )
    )
      return true

    return isCyclical(
      map,
      alsoBlock,
      traverse(map, currentGuardState, alsoBlock),
      visited.apply {
        add(PositionWithDirection(currentGuardState.position, currentGuardState.directionInDegrees))
      },
    )
  }

  /**
   * Recursively calculates the path the guard will take through the map.
   *
   * @param currentGuardState The current state of the guard.
   * @return The final state of the guard after traversing the map.
   */
  private tailrec fun path(currentGuardState: Guard): Guard {
    if (!initialMap.isInBounds(currentGuardState.position)) return currentGuardState

    return path(traverse(initialMap, currentGuardState))
  }

  /**
   * Calculates the next state of the guard based on the current state and the map.
   *
   * @param map The map the guard is traversing.
   * @param currentGuardState The current state of the guard.
   * @param alsoBlock An optional position to consider as an obstruction.
   * @return The updated state of the guard after one step.
   */
  private fun traverse(
    map: List<List<Int>>,
    currentGuardState: Guard,
    alsoBlock: Position? = null,
  ) =
    currentGuardState.let {
      val nextPositionForDirection =
        nextPositionForDirection(currentGuardState.position, currentGuardState.directionInDegrees)
      val canMove =
        map.getOrNull(nextPositionForDirection.y)?.getOrNull(nextPositionForDirection.x) != 1 ||
          alsoBlock == nextPositionForDirection

      currentGuardState.copy(
        position = if (canMove) nextPositionForDirection else currentGuardState.position,
        directionInDegrees =
          if (canMove) currentGuardState.directionInDegrees
          else (currentGuardState.directionInDegrees + 90) % 360,
        visitedPositions = currentGuardState.visitedPositions + currentGuardState.position,
      )
    }

  /**
   * Calculates the next position based on the current position and direction.
   *
   * @param position The current position.
   * @param directionInDegrees The current direction in degrees.
   * @return The next position.
   * @throws IllegalArgumentException if the direction is invalid.
   */
  private fun nextPositionForDirection(position: Position, directionInDegrees: Int) =
    when (directionInDegrees) {
      0 -> position.copy(y = position.y - 1)
      90 -> position.copy(x = position.x + 1)
      180 -> position.copy(y = position.y + 1)
      270 -> position.copy(x = position.x - 1)
      else -> throw IllegalArgumentException("Invalid direction: $directionInDegrees")
    }

  /**
   * Represents the state of the guard.
   *
   * @property position The current position of the guard.
   * @property directionInDegrees The current direction of the guard in degrees.
   * @property visitedPositions A list of positions the guard has visited.
   */
  private data class Guard(
    val position: Position,
    val directionInDegrees: Int,
    val visitedPositions: List<Position> = emptyList(),
  )

  /**
   * Represents a position on the map.
   *
   * @property x The x-coordinate of the position.
   * @property y The y-coordinate of the position.
   */
  data class Position(val x: Int, val y: Int)

  /**
   * Represents a position and direction.
   *
   * @property position The position.
   * @property direction The direction in degrees.
   */
  data class PositionWithDirection(val position: Position, val direction: Int)

  /**
   * Checks if a given position is within the bounds of the [List<List<*>>].
   *
   * @param position The position to check.
   * @return True if the position is within bounds, false otherwise.
   */
  private fun List<List<Any>>.isInBounds(position: Position) =
    position.y in indices && position.x in this[position.y].indices

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val guardGallivant = fromResource("day6/input.txt")
      println(
        """
        Number of visited positions: ${guardGallivant.predictPath().size}
        Number of cyclical paths constructed: ${guardGallivant.findCyclicalPaths().size}
        """
      )
    }

    fun fromResource(path: String): GuardGallivant = fromString(readResourceAsStringList(path)!!)

    fun fromString(lines: List<String>): GuardGallivant {
      val guardPosition: Position =
        lines.foldIndexed(Position(0, 0)) { index, position, line ->
          position.let {
            if (line.indexOf("^") >= 0) Position(line.indexOf("^"), index) else position
          }
        }
      val map = lines.map { line -> line.map { if (it == '#') 1 else 0 } }

      return GuardGallivant(Guard(guardPosition, directionInDegrees = 0), map)
    }
  }
}
