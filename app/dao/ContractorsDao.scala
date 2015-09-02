package dao

import javax.inject.{Inject, Singleton}

import models.{Trade, Contractor}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

@Singleton()
class ContractorsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, protected val tradesDao: TradesDao) extends TradeComponent
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val contractors = TableQuery[Contractors]

  val contractorTrades = TableQuery[ContractorTrades]

  val contractorTradesQuery = for {
    contractor <- contractors
    trade <- contractor.trades
  } yield(contractor, trade)

  // damn, terrible join...
  def all():Future[Seq[models.Contractor]] = db.run(contractorTradesQuery
    .result
    .map {
      _.groupBy(_._1)
    .map {
        case (k, v) => {
          k.Trades = v.map(_._2)
          k
        }
      }.toSeq
  })

  // TODO: don't fetch everything!!!
  def get(id:Long):Option[Contractor] =
    Await.result(all(), Duration.Inf).filter(_.id == id).headOption

  case class ContractorTrade(contractorId:Long, tradeId:Long)

  class ContractorTrades(tag:Tag) extends Table[ContractorTrade](tag, "ContractorTrades") {
    def contractorId = column[Long]("ContractorId")
    def tradeId = column[Long]("TradeId")

    def * = (contractorId, tradeId) <> (ContractorTrade.tupled, ContractorTrade.unapply)

    def tradeFk = foreignKey("TradeId", tradeId, tradesDao.trades)(t => t.id)
    def contractorFk = foreignKey("ContractorId", contractorId, contractors)(c => c.id)
  }

  class Contractors(tag:Tag) extends Table[Contractor](tag, "Contractors") {
    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("Name")
    def email = column[String]("Email")
    def phone = column[String]("Phone")
    def website = column[String]("Website")
    def postcode = column[String]("Postcode")
    def description = column[String]("Description")

    def trades = contractorTrades.filter(_.contractorId === id).flatMap(_.tradeFk)

    override def * = (id, name, email, phone, website, postcode, description) <> ((Contractor.apply _).tupled, Contractor.unapply)
  }
}
