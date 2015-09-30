package dao

import scala.concurrent.Future

import javax.inject.{Inject, Singleton}
import models.Portfolio
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton()
class PortfoliosDao @Inject()(protected val dbConfigProvider:DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  val portfolios = TableQuery[Portfolios]

  def getByContractor(contractorId:Long):Future[Seq[Portfolio]] =
    db.run(portfolios.filter(c => c.contractorId === contractorId).result)

  def insert(portfolio:Portfolio) = {
    val insertion = (portfolios returning portfolios.map(_.id)) += portfolio
    val inserted = db.run(insertion)
    inserted.map { resultId =>
      portfolio.copy(id = Some(resultId))
    }
  }

  protected class Portfolios(tag: Tag) extends Table[Portfolio](tag, "Portfolios") {
    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def contractorId = column[Long]("ContractorId")
    def tradeId = column[Long]("TradeId")
    def shortDescription = column[String]("ShortDescription")
    def longDescription = column[String]("LongDescription")

    override def * = (id.?, contractorId, tradeId, shortDescription, longDescription) <>
      ((Portfolio.apply _).tupled, Portfolio.unapply)
  }
}
