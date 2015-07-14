name := "yabb-frontend"

scalaVersion := "2.11.6"

enablePlugins(ScalaJSPlugin)

unmanagedSourceDirectories in Compile += baseDirectory.value / "../shared" 


