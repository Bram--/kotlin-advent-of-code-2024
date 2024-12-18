package advent.shared

import java.math.BigInteger
import java.security.MessageDigest

/** Converts string to md5 hash. */
fun String.md5() =
  BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/** The cleaner shorthand for printing output. */
fun Any?.printFormatted(format: String = "%s") = println(String.format(format, this))

/** Reads input from resource file, returns null if resource cannot be found.. */
fun Any.readResourceAsStringList(path: String): List<String>? =
  this::class.java.classLoader.getResourceAsStream(path)?.bufferedReader()?.readLines()

/** Returns a list containing all elements except the first one. */
fun <T> List<T>.dropFirst() = this.subList(1, size)

/**
 * Returns the sum of all elements in the list, applying the provided [operation] to each element
 * and its index.
 */
fun <T> List<T>.sumIndexed(operation: (Int, T) -> Int): Int =
  foldIndexed(0) { index, count, element -> count + operation(index, element) }

fun <T> List<T>.countIndexed(operation: (Int, T) -> Boolean): Int =
  foldIndexed(0) { index, count, element -> count + if (operation(index, element)) 1 else 0 }
