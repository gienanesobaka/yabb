name := "yabb-backend"

//lazy val root = (project in file("."))

//unmanagedSourceDirectories in Compile += baseDirectory.value / "../igbb-shared"

version       := "0.1"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

fork in run := true

enablePlugins(JettyPlugin)

libraryDependencies ++= {
  val liftVersion = "2.6.2"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftmodules"   %% "lift-jquery-module_2.6" % "2.8",
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty" % "jetty-plus"          % "8.1.7.v20120910"  % "container,test", // For Jetty Config
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar")
  )
}

libraryDependencies ++= {
  val scalaLoggingV = "3.1.0"
  val h2V =  "1.3.176"
  Seq(
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV,
    "com.h2database" % "h2" % h2V,
  )
}


libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.0.0"
)


libraryDependencies += "com.zaxxer" % "HikariCP-java6" % "2.3.9" % "compile"

libraryDependencies += "ch.qos.logback" % "logback-core" % "1.1.2" % "compile"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2" % "compile"

libraryDependencies += "org.slf4j" % "log4j-over-slf4j" % "1.7.7"

//Revolver.settings
