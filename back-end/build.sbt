name := """back-end"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

//Database dependency

libraryDependencies ++= Seq(
  jdbc,
  evolutions,
  "org.postgresql" % "postgresql" % "42.2.2",
  ehcache,
  javaWs,
  "org.projectlombok" % "lombok" % "1.12.6"
)
//libraryDependencies += evolutions
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.1.4.Final"
libraryDependencies += javaJpa
libraryDependencies += "org.bgee.log4jdbc-log4j2" % "log4jdbc-log4j2-jdbc4.1" % "1.16"

libraryDependencies +=  "com.amazonaws" % "aws-java-sdk" % "1.11.355"

libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"
PlayKeys.externalizeResources := false

coverageEnabled := true
