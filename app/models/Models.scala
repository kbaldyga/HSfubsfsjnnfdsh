package models

import play.api.libs.json.Json

case class Trade(id: Long, name: String)
object Trade { implicit val tradeFormat = Json.format[Trade] }

case class Contractor(id: Option[Long],
                      name: String,
                      email: String,
                      phone: String,
                      website: String,
                      postcode: Int,
                      postcodeSuffix: String,
                      description: Option[String],
                      longDescription: Option[String]
                      ) {
}

case class Postcode(Id: Int, Postcode: Int, City: String, Latitude: Double, Longitude: Double)
object Postcode { implicit val postcodeFormat = Json.format[Postcode] }

object Contractor { implicit val contractorForm = Json.format[Contractor] }
