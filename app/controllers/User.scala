package controllers

import dao.Accounts
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import models.Account
import models.Role.NormalUser
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class User extends Controller with LoginLogout with AuthConfigImpl {

  def login = Action { implicit request =>
    Ok(views.html.index("test"))
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded // this sets cookie to ""
  }

  def authenticate = Action.async(parse.json) { implicit request =>
    val loginResult = request.body.validate[models.Login]
    loginResult.fold(error => Future.successful(BadRequest(toJson("bad request"))),
    login =>
      Accounts.authenticate(login.username, login.password) flatMap {
        case None => Future.successful(Forbidden(toJson("bad username/password")))
        case Some(u) => gotoLoginSucceeded(u.id.get)
      }
    )
  }

  def createUser  = Action.async(parse.json) { implicit request =>
    val loginResult = request.body.validate[models.Login]
    loginResult.fold(error => Future.successful(BadRequest(toJson("bad request"))),
      login =>
        Accounts.create(new Account(None, login.username, login.password, NormalUser)) flatMap {
          u => gotoLoginSucceeded(u.id.get)
        }
    )
  }
}


class Test extends Controller with AuthElement with AuthConfigImpl {
  def main = StackAction(AuthorityKey -> NormalUser) { request =>
    val title = "normal user"
    Ok(toJson(title))
  }
}