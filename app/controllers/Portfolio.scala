package controllers

import javax.inject.Inject
import dao.PortfoliosDao
import dao.{AccountsDao, ContractorsDao}
import jp.t2v.lab.play2.auth.AuthElement
import models.Role.NormalUser
import play.api.libs.json._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.Json.toJson
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Portfolio @Inject()(portfoliosDao: PortfoliosDao, contractorsDao: ContractorsDao, accountsDao: AccountsDao)
  extends Controller with AuthElement with AuthConfigImpl {
  override var accounts: AccountsDao = accountsDao

  def index(contractorId: Long) = Action.async {
    portfoliosDao.getByContractor(contractorId).map {
      case reviews => Ok(toJson(reviews))
    }
  }

  def post(contractorId:Long) = AsyncStack(parse.json, AuthorityKey -> NormalUser) { implicit request =>
    val postPortfolio = request.body.validate[models.PostPortfolio]
    postPortfolio.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors))))
      },
      portfolio => {
        val contractor = Await.result(contractorsDao.getByUserId(loggedIn.id.get), Duration.Inf).headOption
        contractor match {
          case None => Future.successful(BadRequest(toJson("invalid contractor")))
          case Some(c) =>
            if(contractorId != c.id.get) {
              Future.successful(Forbidden(toJson("only owner of the profile can update portfolios")))
            } else {
              val newPortfolio = new models.Portfolio(None, c.id.get, portfolio.tradeId,
                portfolio.shortDescription, portfolio.longDescription)
              portfoliosDao.insert(newPortfolio).map(r => Created(toJson(r)))
            }
        }
      }
    )
  }
}
