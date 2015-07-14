name := "yabb"

//sbtVersion := "0.13.8"

lazy val root = (project in file(".")).aggregate(frontend, backend)

lazy val frontend = project.in(file("frontend"))

lazy val backend = project.in(file("backend"))

run in Compile <<= (run in Compile in backend)