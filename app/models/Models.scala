package models

import play.api.libs.json.{JsValue, Writes, Json}

case class Trade(id: Long, name: String)
object Trade {
  implicit val tradeFormat = Json.format[Trade]
}

case class Contractor(id: Long,
                      name: String,
                      email: String,
                      phone: String,
                      website: String,
                      postcode: String,
                      description: String
                      ) {
  //var Trades:Seq[Trade] = Seq(Trade(10, "test"))
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
