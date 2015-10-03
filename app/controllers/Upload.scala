package controllers

import java.io.File
import javax.inject.Inject
import dao.{AccountsDao, ContractorsDao, UploadsDao}
import jp.t2v.lab.play2.auth.AuthElement
import models.Role.NormalUser
import play.api.libs.json._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Upload @Inject()(uploadsDao: UploadsDao, contractorsDao: ContractorsDao, accountsDao: AccountsDao)
  extends Controller with AuthElement with AuthConfigImpl {
  override var accounts: AccountsDao = accountsDao

  def index = Action.async { implicit request =>
    uploadsDao.getByUser(loggedIn.id.get).map {
      case uploads => Ok(toJson(uploads))
    }
  }

  def get(id: Int) = Action.async {
    uploadsDao.get(id).map {
      case None => NotFound
      case Some(u) => Ok(toJson(u))
    }
  }

  def post = AsyncStack(parse.temporaryFile, AuthorityKey -> NormalUser) { implicit request =>
    val userId = loggedIn.id.get
    val fileName = java.util.UUID.randomUUID.toString
    val file = request.body.moveTo(new File("uploads/" + fileName))
    uploadsDao.insert(new models.Upload(None, 0, userId, "portfolio", file.getCanonicalPath, None))
      .map { upload => Ok(toJson(upload))}
  }

  def delete(id: Int) = AsyncStack(parse.json, AuthorityKey -> NormalUser) { implicit request =>
    uploadsDao.get(id).map {
      case None => NotFound
      case Some(u) =>
        if (u.createdBy != loggedIn.id.get) {
          Forbidden
        }
        else {
          // delete file from disk
          Await.result(uploadsDao.delete(u).map {
            _ => Ok("please implement delete functionality")
          }, Duration.Inf)
        }
    }
  }
}
