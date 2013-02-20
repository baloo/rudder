
import sbt._
import Keys._

object RudderBuild extends Build {

  object Resolvers {
    val rudderRepo = "rudder repository" at "http://www.rudder-project.org/maven-repo/"

    val default = Seq(
      rudderRepo
      )
  }

  object Dependencies {
    val liftVersion = "2.4"

    val springRunDependencies = Seq(
      "spring-run-dependencies" % "com.normation" % "1.0.1"
    )

    val runtime = Seq(
      "joda-time"      %  "joda-time"        % "2.1",
      "org.joda"       %  "joda-convert"     % "1.2"    % "provided",

      ("net.liftweb"   % "lift-common_2.9.1" % liftVersion
        ).exclude("org.slf4j", "slf4j-log4j12"
        ).exclude("log4j", "log4j"),

      "org.slf4j"      %  "slf4j-api"        % "1.7.2",
      "ch.qos.logback" %  "logback-core"     % "1.0.7",
      "ch.qos.logback" %  "logback-classic"  % "1.0.7",

      "junit"          %  "junit"            % "4.11"   % "test",
      "org.specs2"     %% "specs2"           % "1.12.3" % "test"
    )

    val core = Seq(
      "com.normation.inventory" %  "inventory-api"        % CommonSettings.rudderVersion,
      "com.normation.inventory" %  "inventory-repository" % CommonSettings.rudderVersion,
      "com.normation"           %  "cfclerk"              % CommonSettings.rudderVersion,
      "com.normation"           %  "eventlog-api"         % CommonSettings.rudderVersion,

      "postgresql"              %  "postgresql"           % "8.4-702.jdbc4"   % "runtime",
      "org.squeryl"             %% "squeryl"              % "0.9.5-4",
      "commons-dbcp"            %  "commons-dbcp"         % "1.4",

      ("net.liftweb"   % "lift-webkit_2.9.1" % liftVersion
        ).exclude("org.slf4j", "slf4j-log4j12"
        )
    )
  }

  object CommonSettings {
    val rudderVersion = "2.5.0-SNAPSHOT"

    val settings = Project.defaultSettings ++ Seq(
      organization        :=  "com.normation.rudder",
      version             :=  rudderVersion,
      crossScalaVersions  :=  Seq("2.9.2"),
      libraryDependencies :=  Dependencies.runtime,
      resolvers           ++= Resolvers.default
    )
  }


  lazy val RudderCore = Project(
    id="rudder-core",
    base=file("rudder-core"),
    settings = CommonSettings.settings ++ Seq(
      libraryDependencies := Dependencies.core
    ))

  lazy val RudderWeb = Project(
    id="rudder-web",
    base=file("rudder-web"),
    settings = CommonSettings.settings)

  val Root = Project(
    id="rudder-parent",
    base=file("."),
    settings = Project.defaultSettings
    ).aggregate(RudderWeb, RudderCore)

}

