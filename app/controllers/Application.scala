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


// curl -H "Content-Type: application/json" -X POST -d '{"name":"test name3","email":"test email3@email.cc","phone":"0676787773","website":"test website3","postcode":1061,"description":"long description3", "postcodeSuffix": "CS"}' localhost:9000/api/contractors