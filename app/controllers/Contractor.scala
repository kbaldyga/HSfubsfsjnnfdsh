package controllers

import javax.inject.Inject
import dao.ContractorsDao
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

class Contractor @Inject()(contractorsDao: ContractorsDao) extends Controller {
  def index = Action.async {
    contractorsDao.all().map {
      case Seq() => NotFound
      case c => Ok(toJson(c))
    }
  }

  def get(id: Long) = Action.async {
    contractorsDao.get(id) map {
      case None => NotFound
      case c => Ok(toJson(c))
    }
  }

  def trades(id: Long) = Action.async {
    contractorsDao.tradesByContractor(id) map {
      case Seq() => NotFound
      case t => Ok(toJson(t))
    }
  }

  def post = Action.async(parse.json) { request =>
    request.body.validate[models.Contractor].map { contractor =>
      contractorsDao.insert(contractor).map(newContractor => Ok(toJson(newContractor)))
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def postTrade(id:Long, tradeId:Long) = Action.async {
    contractorsDao.get(id) map {
      case None => NotFound
      case c =>
          contractorsDao.insertTrade(id, tradeId)
          Ok(toJson(c))
    }
  }
}
