package dao

import scala.concurrent.Future

import javax.inject.Inject
import models.Trade
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
class TradeDao @Inject()(protected val dbConfigProvider:DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

}
