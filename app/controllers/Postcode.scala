package controllers

import javax.inject.Inject
import dao.PostcodesDao
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Postcode @Inject()(postcodesDao:PostcodesDao) extends Controller {
  def index(prefix: Option[String]) = Action.async {
    prefix match {
      case Some(c) =>
        val intPrefix = (if (c.length() > 4) c.substring(0, 4) else c).toInt
        postcodesDao.find(intPrefix).map {
        case res => Ok(toJson(res))
      }
      case None =>
        postcodesDao.all().map {
          case Seq() => NotFound
          case c => Ok(toJson(c))
        }
    }
  }

  def get(id: Int) = Action.async {
    postcodesDao.get(id).map {
      case None => NotFound
      case c => Ok(toJson(c))
    }
  }

  def nearest(id: Int, maxDistance:Int) = Action.async {
    postcodesDao.findNearest(id, maxDistance) map {
      case res => Ok(toJson(res))
    }
  }
}
