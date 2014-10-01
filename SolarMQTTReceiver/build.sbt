import AssemblyKeys._

seq(assemblySettings: _*)

mergeStrategy in assembly := {
  case PathList("org", "datanucleus", xs @ _*)             => MergeStrategy.discard
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "log4j.properties"                                  => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}

name := "receiver"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.spark"      %% "spark-core"           % "1.1.0",
  "org.apache.spark"      %% "spark-streaming"      % "1.1.0",
  "org.apache.spark"      %% "spark-streaming-mqtt" % "1.1.0",
  "com.datastax.spark" %% "spark-cassandra-connector" % "1.1.0-alpha1" withSources() withJavadoc()
)

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Eclipse Paho Repo" at "https://repo.eclipse.org/content/repositories/paho-releases/"
)
