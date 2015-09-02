package controllers

import javax.inject.Inject
import dao.ContractorsDao
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Contractor @Inject()(contractorsDao: ContractorsDao) extends Controller {
  def index = Action.async {
    contractorsDao.all().map {
      case Seq() => NotFound
      case c => Ok(toJson(c))
    }
  }

  // TODO: make async
  def get(id: Long) = Action {
    contractorsDao.get(id) match {
      case None => NotFound
      case c => Ok(toJson(c))
    }
  }
}
