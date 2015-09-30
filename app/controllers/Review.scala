package controllers

import javax.inject.Inject
import dao.{AccountsDao, ContractorsDao, ReviewsDao}
import jp.t2v.lab.play2.auth.AuthElement
import models.Role.NormalUser
import play.api.libs.json._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Review @Inject()(reviewsDao: ReviewsDao, contractorsDao: ContractorsDao, accountsDao: AccountsDao)
  extends Controller with AuthElement with AuthConfigImpl {
  override var accounts: AccountsDao = accountsDao

  def index(contractorId: Long) = Action.async {
    reviewsDao.getByContractor(contractorId).map {
      case reviews => Ok(toJson(reviews))
    }
  }

  def post(contractorId: Long) = AsyncStack(parse.json, AuthorityKey -> NormalUser) { implicit request =>
    val reviewResult = request.body.validate[models.PostReview]
    reviewResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors))))
      },
      review => {
        val contractor = Await.result(contractorsDao.get(contractorId), Duration.Inf).headOption
        contractor match {
          case None => Future.successful(BadRequest(toJson("invalid contractor")))
          case Some(_) =>
            val newReview = new models.Review(None, contractorId, loggedIn.id.get,
              review.rating, review.shortDescription, review.longDescription)
            reviewsDao.insert(newReview).map(r => Created(toJson(r)))
        }
      }
    )
  }

  def getMyReviews() = AsyncStack(AuthorityKey -> NormalUser) { implicit  request =>
    reviewsDao.getByUser(loggedIn.id.get).map { case r => Ok(toJson(r)) }
  }

  def getByUser(userId: Long) = Action.async {
    reviewsDao.getByUser(userId).map {
      case r => Ok(toJson(r))
    }
  }
}
