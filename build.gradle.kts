plugins {
  kotlin("jvm") version "2.1.0"

  id("com.ncorti.ktfmt.gradle") version "0.21.0"
}

dependencies {
  // Use Google's Truth for asseertions
  testImplementation("com.google.truth:truth:1.4.4")

  // Junit 5
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

ktfmt {
  // Google style - 2 space indentation & automatically adds/removes trailing commas
  googleStyle()

  // Automatically removes any unused imports.
  removeUnusedImports.set(true)
}


tasks {
  wrapper { gradleVersion = "8.11.1" }

  test { useJUnitPlatform() }
}

// Day 1
task("HistorianHysteria", JavaExec::class) {
  group = "advents"
  mainClass = "advent.days.day1.HistorianHysteria"
  classpath = sourceSets["main"].runtimeClasspath
}

// Day 2
task("RedNoseReports", JavaExec::class) {
  group = "advents"
  mainClass = "advent.days.day2.RedNoseReports"
  classpath = sourceSets["main"].runtimeClasspath
}

// Day 3
task("MullItOver", JavaExec::class) {
  group = "advents"
  mainClass = "advent.days.day3.MullItOver"
  classpath = sourceSets["main"].runtimeClasspath
}

// Day 4
task("CeresSearch", JavaExec::class) {
  group = "advents"
  mainClass = "advent.days.day4.CeresSearch"
  classpath = sourceSets["main"].runtimeClasspath
}
