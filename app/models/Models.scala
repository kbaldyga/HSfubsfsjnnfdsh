package models

import play.api.libs.json.{JsValue, Writes, Json}

case class Trade(id: Long, name: String)
object Trade {
  implicit val tradeFormat = Json.format[Trade]
}

case class Contractor(id: Option[Long],
                      name: String,
                      email: String,
                      phone: String,
                      website: String,
                      postcode: Int,
                      postcodeSuffix: String,
                      description: Option[String]
                      ) {
  //var Trades:Seq[Trade] = Seq(Trade(10, "test"))
}

case class Postcode(Id: Int, Postcode: Int, City: String, Latitude: Double, Longitude: Double)
object Postcode {
  implicit val postcodeFormat = Json.format[Postcode]
}

object Contractor {
  implicit val contractorForm = Json.format[Contractor]
//  implicit val contractorWrites = new Writes[Contractor] {
//    override def writes(o: Contractor): JsValue = Json.obj(
//      "id" -> o.id,
//      "name" -> o.name,
//      "trades" -> Json.toJson(o.Trades),
//      "email" -> o.email,
//      "phone" -> o.phone,
//      "website" -> o.website,
//      "postcode" -> o.postcode,
//      "description" -> o.description
//    )
//  }
}
