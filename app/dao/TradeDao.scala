package dao


import scala.concurrent.Future

import javax.inject.{Inject, Singleton}
import models.Trade
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile


trait TradeComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import driver.api._

  class Trades(tag: Tag) extends Table[Trade](tag, "Trades") {
    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("Name")

    def * = (id, name).shaped <> ((Trade.apply _).tupled, Trade.unapply)
  }
}

@Singleton()
class TradesDao @Inject()(protected val dbConfigProvider:DatabaseConfigProvider) extends TradeComponent
  with HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  val trades = TableQuery[Trades]

  def all():Future[Seq[Trade]] = db.run(trades.result)

  def get(id:Long):Future[Option[Trade]] = db.run(trades.filter(_.id === id).result.headOption)
}
//class TradeDao @Inject()(protected val dbConfigProvider:DatabaseConfigProvider) extends
//  HasDatabaseConfigProvider[JdbcProfile] {
//
//  import driver.api._
//
//  private val Trades = TableQuery[TradesTable]
//
//  def all():Future[Seq[Trade]] = db.run(Trades.result)
//
//  def insert(trade: Trade):Future[Unit] = db.run(Trades += trade).map(_ => ())
//
//  def get(id: Long):Future[Option[Trade]] = db.run(Trades.filter(_.id === id).result.headOption)
//
//  private class TradesTable(tag: Tag) extends Table[Trade](tag, "Trade") {
//
//    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
//    def name = column[String]("Name")
//
//    def * = (id, name).shaped <> ((Trade.apply _).tupled, Trade.unapply)
//  }
//
//}
