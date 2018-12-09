// @GENERATOR:play-routes-compiler
// @SOURCE:/home/dionisio/eclipse-workspace-concorrente/java-play-akka-spark-cassandra/conf/routes
// @DATE:Sat Dec 08 23:20:33 BRT 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
