package controllers

import javax.inject.Inject
import dao.{PostcodesDao, ContractorsDao}
import play.api.libs.json._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class Contractor @Inject()(contractorsDao: ContractorsDao, postcodesDao: PostcodesDao) extends Controller {
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
    val contractorResult = request.body.validate[models.Contractor]
    contractorResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors))))
      },
      contractor => {
        val postcodePrefix = (if (contractor.postcode.length() > 4) contractor.postcode.substring(0, 4) else contractor.postcode).toInt
        val postCode = Await.result(postcodesDao.find(postcodePrefix), Duration.Inf).headOption
        postCode match {
          case None => Future.successful(BadRequest(toJson("invalid postcode")))
          case Some(s) => contractorsDao.insert(contractor.copy(postcode = postCode.get.Postcode.toString))
            .map(newContractor => Ok(toJson(newContractor)))
        }
      }
    )
  }

  def details(id:Long) = Action.async(parse.json) { request =>
    request.body match {
      case JsObject(fields) =>
          fields.getOrElse("description", None) match {
            case None => { }
            case JsString(s) =>
              contractorsDao.updateDescription(id, s.toString)
          }
        Future.successful(Ok(toJson("Ok")))
      case _ => Future.successful(BadRequest(toJson("bad request")))
    }
  }

  def putTrades(id:Long) = Action.async(parse.json) { request =>
    request.body match {
      case JsArray(a) =>
        val newTrades = a.map(_ match {
          case JsString(x) => x.toInt
          case _ => 0
        }).filter(_ > 0)
        contractorsDao.updateTrades(id, newTrades)
      case _ => { }
    }
    Future.successful(Ok(toJson("Ok")))
  }
}
