package controllers

import javax.inject.Inject
import play.api.mvc.Action
import play.api.mvc.Controller

class Trade  extends Controller {


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
