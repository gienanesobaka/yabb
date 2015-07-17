name := "yabb-frontend"

version       := "0.1"

scalaVersion  := "2.11.7"

enablePlugins(ScalaJSPlugin)

unmanagedSourceDirectories in Compile += baseDirectory.value / "../shared"

libraryDependencies += "biz.enef" %%% "scalajs-angulate" % "0.2.1"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.3.4"


//jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular.min.js"


jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular.min.js"

jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular-cookies.min.js" dependsOn "angular.min.js"

jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular-route.min.js" dependsOn "angular.min.js"

jsDependencies += "org.webjars" % "jquery" % "1.9.1" / "jquery.min.js"

//jsDependencies += "org.webjars" % "angular-ui-router" % "0.2.15" / "angular-ui-router.min.js" dependsOn "angular.min.js"

persistLauncher in Compile := true
skip in packageJSDependencies := false

mainClass := Some("gie.yabb.app")


