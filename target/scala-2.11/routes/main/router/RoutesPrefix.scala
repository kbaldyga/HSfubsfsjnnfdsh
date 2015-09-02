
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/kamil/github/hireable/conf/routes
// @DATE:Wed Sep 02 16:03:46 CEST 2015


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
