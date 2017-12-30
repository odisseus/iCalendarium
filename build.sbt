import Dependencies._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += hocon,
    libraryDependencies += parsing,
    ScalariformKeys.preferences := scalariformPreferences
)

import scalariform.formatter.preferences._

scalariformSettings

lazy val scalariformPreferences = FormattingPreferences()
  .setPreference(PreserveSpaceBeforeArguments, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
