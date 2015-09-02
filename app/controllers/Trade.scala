package controllers

import javax.inject.Inject
import dao.TradesDao
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Trade @Inject()(tradeDao: TradesDao) extends Controller {
  def index = Action.async {
    tradeDao.all().map {
      case Seq() => NotFound
      case trades => Ok(toJson(trades))
    }
  }

  def get(id: Long) = Action.async {
    tradeDao.get(id).map {
      case None => NotFound
      case Some(trade) => Ok(toJson(trade))
    }
  }
}
