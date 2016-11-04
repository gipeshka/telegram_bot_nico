resolvers += "jitpack" at "https://jitpack.io"

lazy val root = (project in file(".")).
  settings(
    name := "FP telegram bot",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.github.mukel" %% "telegrambot4s" % "v1.2.2"
    )
  )
