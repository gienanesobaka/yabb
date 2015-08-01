name := "yabb-frontend"

version       := "0.1"

scalaVersion  := "2.11.7"

enablePlugins(ScalaJSPlugin)

unmanagedSourceDirectories in Compile += baseDirectory.value / "../shared"

libraryDependencies += "biz.enef" %%% "scalajs-angulate" % "0.2.1"


libraryDependencies += "biz.enef" %%% "slogging" % "0.3"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.3.4"


//jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular.min.js"


jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular.min.js"

jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular-cookies.min.js" dependsOn "angular.min.js"

jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular-route.min.js" dependsOn "angular.min.js"

jsDependencies += "org.webjars" % "jquery" % "1.9.1" / "jquery.min.js"

jsDependencies += ProvidedJS / "validate.min.js" dependsOn "angular.min.js"

jsDependencies += ProvidedJS / "bootstrap.min.js" dependsOn "jquery.min.js"

jsDependencies += ProvidedJS / "ui-bootstrap.min.js" dependsOn ("angular.min.js","bootstrap.min.js")

jsDependencies += ProvidedJS / "abn_tree_directive.js" dependsOn ("angular.min.js","bootstrap.min.js")

persistLauncher in Compile := true
skip in packageJSDependencies := false

mainClass := Some("gie.yabb.app")


