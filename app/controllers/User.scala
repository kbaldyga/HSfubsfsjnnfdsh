package controllers

import javax.inject.Inject
import authentication.AuthConfigImpl
import dao.Accounts
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.Account
import models.Role.NormalUser
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class User extends Controller with LoginLogout with AuthConfigImpl {

  def login = Action { implicit request =>
    Ok(views.html.index("test"))
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded
  }

  def authenticate = Action.async(parse.json) { implicit request =>
    val loginResult = request.body.validate[models.Login]
    // TODO: get user by username from loginResult
    Accounts.create(new Account(None, "email", "password", NormalUser))
    gotoLoginSucceeded(100)
  }

//    loginForm.bindFromRequest.fold(
//      formWithErrors => BadRequest(html.login(formWithErrors)),
//      user => gotoLoginSucceeded(user.get.id)
//    )
}


class Test extends Controller with AuthElement with AuthConfigImpl {
  def main = StackAction(AuthorityKey -> NormalUser) { request =>
    val title = "normal user"
    Ok(toJson(title))
  }
}