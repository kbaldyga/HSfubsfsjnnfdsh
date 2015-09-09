package controllers

import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome"))
  }

  def playIndex = Action {
    Ok(views.html.playIndex("Your new application is ready."))
  }

  def contractorForm1 = Action {
    Ok(views.html.contractor1("Welcome to hireable"))
  }
}
