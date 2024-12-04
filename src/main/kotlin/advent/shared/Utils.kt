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
