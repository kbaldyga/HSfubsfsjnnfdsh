package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome"))
  }

  def playIndex = Action {
    Ok(views.html.playIndex("Your new application is ready."))
  }

}
