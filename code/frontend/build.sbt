name := "yabb-frontend"

version       := "0.1"

scalaVersion  := "2.11.7"

enablePlugins(ScalaJSPlugin)

unmanagedSourceDirectories in Compile += baseDirectory.value / "../shared" 


