
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/kamil/github/hireable/conf/routes
// @DATE:Wed Sep 02 16:03:46 CEST 2015

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  Application_1: controllers.Application,
  // @LINE:7
  Trade_2: controllers.Trade,
  // @LINE:10
  Contractor_3: controllers.Contractor,
  // @LINE:14
  Assets_0: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    Application_1: controllers.Application,
    // @LINE:7
    Trade_2: controllers.Trade,
    // @LINE:10
    Contractor_3: controllers.Contractor,
    // @LINE:14
    Assets_0: controllers.Assets
  ) = this(errorHandler, Application_1, Trade_2, Contractor_3, Assets_0, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, Application_1, Trade_2, Contractor_3, Assets_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.Application.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """trade""", """controllers.Trade.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """trade/$id<[^/]+>""", """controllers.Trade.get(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """contractor""", """controllers.Contractor.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """contractor/$id<[^/]+>""", """controllers.Contractor.get(id:Long)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_Application_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_Application_index0_invoker = createInvoker(
    Application_1.index,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Application",
      "index",
      Nil,
      "GET",
      """ Home page""",
      this.prefix + """"""
    )
  )

  // @LINE:7
  private[this] lazy val controllers_Trade_index1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("trade")))
  )
  private[this] lazy val controllers_Trade_index1_invoker = createInvoker(
    Trade_2.index,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Trade",
      "index",
      Nil,
      "GET",
      """""",
      this.prefix + """trade"""
    )
  )

  // @LINE:8
  private[this] lazy val controllers_Trade_get2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("trade/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_Trade_get2_invoker = createInvoker(
    Trade_2.get(fakeValue[Long]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Trade",
      "get",
      Seq(classOf[Long]),
      "GET",
      """""",
      this.prefix + """trade/$id<[^/]+>"""
    )
  )

  // @LINE:10
  private[this] lazy val controllers_Contractor_index3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("contractor")))
  )
  private[this] lazy val controllers_Contractor_index3_invoker = createInvoker(
    Contractor_3.index,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Contractor",
      "index",
      Nil,
      "GET",
      """""",
      this.prefix + """contractor"""
    )
  )

  // @LINE:11
  private[this] lazy val controllers_Contractor_get4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("contractor/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_Contractor_get4_invoker = createInvoker(
    Contractor_3.get(fakeValue[Long]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Contractor",
      "get",
      Seq(classOf[Long]),
      "GET",
      """""",
      this.prefix + """contractor/$id<[^/]+>"""
    )
  )

  // @LINE:14
  private[this] lazy val controllers_Assets_versioned5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned5_invoker = createInvoker(
    Assets_0.versioned(fakeValue[String], fakeValue[Asset]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      """ Map static resources from the /public folder to the /assets URL path""",
      this.prefix + """assets/$file<.+>"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_Application_index0_route(params) =>
      call { 
        controllers_Application_index0_invoker.call(Application_1.index)
      }
  
    // @LINE:7
    case controllers_Trade_index1_route(params) =>
      call { 
        controllers_Trade_index1_invoker.call(Trade_2.index)
      }
  
    // @LINE:8
    case controllers_Trade_get2_route(params) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_Trade_get2_invoker.call(Trade_2.get(id))
      }
  
    // @LINE:10
    case controllers_Contractor_index3_route(params) =>
      call { 
        controllers_Contractor_index3_invoker.call(Contractor_3.index)
      }
  
    // @LINE:11
    case controllers_Contractor_get4_route(params) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_Contractor_get4_invoker.call(Contractor_3.get(id))
      }
  
    // @LINE:14
    case controllers_Assets_versioned5_route(params) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned5_invoker.call(Assets_0.versioned(path, file))
      }
  }
}