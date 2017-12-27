import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val hocon = "com.typesafe" % "config" % "1.3.1"
  lazy val parsing = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
}
