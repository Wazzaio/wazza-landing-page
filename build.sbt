lazy val appName = "Wazza-landing-page"

lazy val appVersion = "2015.05"

scalaVersion := "2.11.7"

lazy val dependencies = Seq(
  cache,
  filters,
  jdbc,
  ws,
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "font-awesome" % "4.3.0-2",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "normalize.css" % "3.0.2",
  "org.webjars" %% "webjars-play" % "2.4.0-1"
)

libraryDependencies ++= dependencies

resolvers ++= Seq[Resolver](
    DefaultMavenRepository,
    Classpaths.typesafeReleases,
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    Classpaths.sbtPluginReleases,
    "Eclipse repositories" at "https://repo.eclipse.org/service/local/repositories/egit-releases/content/",
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )

pipelineStages := Seq(closure, cssCompress, htmlMinifier, /**imagemin,**/ gzip) //sbt-compat

lazy val mySettings = Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls", "-language:postfixOps", "-optimize", "-Ywarn-adapted-args", "-Xlint", "-Xfatal-warnings")

// Root
lazy val home = Project(appName, file("."))
  .enablePlugins(play.PlayScala)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)
