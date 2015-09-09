package dao

import javax.inject.{Inject, Singleton}

import models.Postcode
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton()
class PostcodesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends PostcodesComponent
with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val postcodes = TableQuery[Postcodes]

  def all():Future[Seq[Postcode]] = db.run(postcodes.result)

  def get(id:Int):Future[Option[Postcode]] =
    db.run(postcodes.filter(_.id === id).result.headOption)

  private def prefixQuery(prefix:Int) = for {
    c <- postcodes if
      ((c.code >= prefix * 1000) && (c.code < prefix * 1000 + 1000)) ||
        ((c.code >= prefix * 100) && (c.code < prefix * 100 + 100)) ||
        ((c.code >= prefix * 10) && (c.code < prefix * 10 + 10)) ||
        (c.code === prefix)
  } yield c

  def find(prefix:Int):Future[Seq[Postcode]] =
    db.run(prefixQuery(prefix).result)

  private def toRadian(value:Double) = (Math.PI / 180.0) * value
  private def distance(pos1Lat:Double, pos1Lon:Double, pos2Lat:Double, pos2Lon:Double):Double = {
    val earthRadius = 6371
    val dLat = toRadian(pos2Lat - pos1Lat)
    val dLon = toRadian(pos2Lon - pos1Lon)
    val a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) +
      Math.cos(toRadian(pos1Lat)) * Math.cos(toRadian(pos2Lat)) *
      Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0)
    val c = 2.0 * Math.asin(Math.min(1, Math.sqrt(a)))
    val result = earthRadius * c
    result
  }

  def findNearest(initialId:Int, maxDistance:Int):Future[Seq[Postcode]] = {
    for {
      initial <- get(initialId)
      a <- all()
      within: Seq[Postcode] = a.filter(p =>
        (distance(initial.get.Latitude, initial.get.Longitude, p.Latitude, p.Longitude) < maxDistance))
    } yield(within)
  }
}

trait PostcodesComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import driver.api._

  class Postcodes(tag:Tag) extends Table[Postcode](tag, "PostalCodes") {
    def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)
    def code = column[Int]("Postcode")
    def city = column[String]("Woonplaats")
    def lat = column[Double]("Latitude")
    def lon = column[Double]("Longitude")

    override def * = (id, code, city, lat, lon) <> ((Postcode.apply _).tupled, Postcode.unapply)
  }
}