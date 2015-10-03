package models

import org.joda.time.DateTime
import play.api.libs.json.Json

// TRADE
case class Trade(id: Long, name: String)
object Trade { implicit val tradeFormat = Json.format[Trade] }

// CONTRACTOR
case class Contractor(id: Option[Long],
                      accountId: Long,
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
object Contractor { implicit val contractorForm = Json.format[Contractor] }

// POSTCODE
case class Postcode(Id: Int, Postcode: Int, City: String, Latitude: Double, Longitude: Double)
object Postcode { implicit val postcodeFormat = Json.format[Postcode] }

// REVIEW
case class PostReview(rating: Int, shortDescription: String, longDescription: Option[String])
object PostReview { implicit val format = Json.format[PostReview] }
case class Review(id: Option[Long], contractorId: Long, createdBy: Long, rating: Int,
                  shortDescription: String, longDescription: Option[String])
object Review { implicit val format = Json.format[Review] }

// PORTFOLIO
case class Portfolio(id: Option[Long], contractorId: Long, tradeId: Long,
                     shortDescription: String, longDescription: String)
object Portfolio { implicit val format = Json.format[Portfolio] }
case class PostPortfolio(tradeId: Long, shortDescription: String, longDescription: String)
object PostPortfolio { implicit val format = Json.format[PostPortfolio] }

// UPLOAD
case class Upload(id: Option[Long], aggregateId: Long, createdBy: Long,
                   uploadType: String, physicalLocation: String, createdAt: Option[DateTime])
object Upload { implicit val format = Json.format[Upload] }

//    case "Portfolio" => PortfolioType
//    case "Review"    => ReviewType
//    case "Avatar"   => AvatarTypeArgumentException()