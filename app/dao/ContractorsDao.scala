package dao

import javax.inject.{Inject, Singleton}

import models.{Trade, Contractor}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton()
class ContractorsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, protected val tradesDao: TradesDao)
  extends TradeComponent
  with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val contractors = TableQuery[Contractors]

  val contractorTrades = TableQuery[ContractorTrades]

  val contractorTradesQuery = for {
    contractor <- contractors
    trade <- contractor.trades
  } yield(contractor, trade)

  def all():Future[Seq[Contractor]] = db.run(contractors.result)

  def tradesByContractor(id:Long):Future[Seq[Trade]] = {
    val tradesQuery = for {
      contractor <- contractors if contractor.id === id
      trade <- contractor.trades
    } yield (trade)

    db.run(tradesQuery.result)
  }

  def get(id:Long):Future[Option[Contractor]] =
    db.run(contractors.filter(_.id === id).result.headOption)

  def getByUserId(accountId: Long):Future[Option[Contractor]] =
    db.run(contractors.filter(_.accountId === accountId).result.headOption)

  def insert(contractor:Contractor) = {
    val insertion = (contractors returning contractors.map(_.id)) += contractor
    val inserted = db.run(insertion)
    inserted.map { resultId =>
      contractor.copy(id = Some(resultId))
    }
  }

  def insertTrade(contractorId:Long, tradeId:Long) = {
    db.run(contractorTrades += (ContractorTrade(contractorId, tradeId)))
  }

  def updateTrades(contractorId:Long, newTrades:Seq[Int]) = {
    val oldTrades = for {
      t <- contractorTrades if t.contractorId === contractorId
    } yield(t)

    db.run(oldTrades.delete)
    db.run(contractorTrades ++= (newTrades.map(ContractorTrade(contractorId, _))))
  }

  def updateDescription(id:Long, description:String): Future[Int] = {
    val contractor = for {c <- contractors if c.id === id} yield c.description
    db.run(contractor.update(description))
  }

  def updateLongDescription(id:Long, description:String): Future[Int] = {
    val contractor = for {c <- contractors if c.id === id} yield c.longDescription
    db.run(contractor.update(description))
  }

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
    def accountId = column[Long]("AccountId")
    def name = column[String]("Name")
    def email = column[String]("Email")
    def phone = column[String]("Phone")
    def website = column[String]("Website")
    def postcode = column[Int]("Postcode")
    def postcodeSuffix = column[String]("PostcodeSuffix")
    def description = column[String]("Description")
    def longDescription = column[String]("LongDescription")

    def trades = contractorTrades.filter(_.contractorId === id).flatMap(_.tradeFk)

    override def * = (id.?, accountId, name, email, phone, website, postcode, postcodeSuffix, description.?, longDescription.?) <>
      ((Contractor.apply _).tupled, Contractor.unapply)
  }
}
