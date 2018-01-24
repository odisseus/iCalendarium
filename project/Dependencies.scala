import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val hocon = "com.typesafe" % "config" % "1.3.1"
  lazy val parsing = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
  lazy val jodatime = "joda-time" % "joda-time" % "2.9.9"
  lazy val icalendar = "net.sf.biweekly" % "biweekly" % "0.6.1"
  lazy val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0"
}
