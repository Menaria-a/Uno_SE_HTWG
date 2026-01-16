
val scala3Version = "3.7.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "mau",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    fork := true,
    connectInput := true,
    
    libraryDependencies ++= {
      // Determine OS
      val os = sys.props("os.name").toLowerCase
      val classifier = os match {
        case x if x.contains("win") => "win"
        case x if x.contains("mac") => "mac"
        case x if x.contains("nux") => "linux"
        case _ => ""
      }
      val javafxVersion = "23.0.1"
      Seq(
        "org.scalactic" %% "scalactic" % "3.2.14",
        "org.scalatest" %% "scalatest" % "3.2.14" % "test",
        "org.scalameta" %% "munit" % "1.0.0" % Test,
        "org.scalafx" %% "scalafx" % "23.0.1-R34",
        "com.google.inject" % "guice" % "7.0.0",
        "net.codingwell" %% "scala-guice" % "7.0.0",
        "org.scala-lang.modules" %% "scala-xml" % "2.3.0",
        "org.playframework" %% "play-json" % "3.0.4",
        "org.openjfx" % "javafx-base" % javafxVersion classifier classifier,
        "org.openjfx" % "javafx-controls" % javafxVersion classifier classifier,
        "org.openjfx" % "javafx-graphics" % javafxVersion classifier classifier,
        "org.openjfx" % "javafx-media" % javafxVersion classifier classifier
      )
    },
    coverageExcludedFiles := ".*main|.*Gui.*|.*Tui*"
  )
