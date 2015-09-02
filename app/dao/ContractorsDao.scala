package dao

import javax.inject.{Inject, Singleton}

import models.Contractor
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

@Singleton()
class ContractorsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends TradeComponent
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val contractors = TableQuery[Contractors]

  class Contractors(tag:Tag) extends Table[Contractor](tag, "Contractors") {
    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("Name")
    def email = column[String]("Email")
    def phone = column[String]("Phone")
    def website = column[String]("Website")
    def postcode = column[String]("Postcode")
    def description = column[String]("Description")
//    trades: Seq[Trade] = Seq())

    override def * = (id, name, email, phone, website, postcode, description) <> ((Contractor.apply _).tupled, Contractor.unapply)
  }
}


@Singleton()
class ContractorTrades @Inject()(protected val dbConfigProvider:DatabaseConfigProvider,
                                 tradesDao: TradesDao,
                                 contractorsDao: ContractorsDao) extends TradeComponent
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val contractorTrades = TableQuery[ContractorTrades]

  case class ContractorTrade(contractorId:Long, tradeId:Long)

  class ContractorTrades(tag:Tag) extends Table[ContractorTrade](tag, "ContractorTrades") {
    def contractorId = column[Long]("ContractorId")
    def tradeId = column[Long]("TradeId")

    def * = (contractorId, tradeId) <> (ContractorTrade.tupled, ContractorTrade.unapply)

    def tradeFk = foreignKey("TradeId", tradeId, tradesDao.trades)(t => t.id)
    def contractorFk = foreignKey("ContractorId", contractorId, contractorsDao.contractors)(c => c.id)
  }
}