package models

import play.api.libs.json.Json

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
                      //,trades: Seq[Trade] = Seq())
                      )

object Contractor {
  implicit val contractorForm = Json.format[Contractor]
}

