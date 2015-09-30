package dao

import scala.concurrent.Future

import javax.inject.{Inject, Singleton}
import models.Review
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton()
class ReviewsDao @Inject()(protected val dbConfigProvider:DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  val reviews = TableQuery[Reviews]

  def getByContractor(contractorId:Long):Future[Seq[Review]] =
    db.run(reviews.filter(c => c.contractorId === contractorId).result)

  def insert(review:Review) = {
    val insertion = (reviews returning reviews.map(_.id)) += review
    val inserted = db.run(insertion)
    inserted.map { resultId =>
      review.copy(id = Some(resultId))
    }
  }

  def getByUser(userId: Long): Future[Seq[Review]] =
    db.run(reviews.filter(_.createdBy === userId).result)


  protected class Reviews(tag: Tag) extends Table[Review](tag, "Reviews") {
    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def contractorId = column[Long]("ContractorId")
    def createdBy = column[Long]("CreatedBy")
    def rating = column[Int]("Rating")
    def shortDescription = column[String]("ShortDescription")
    def longDescription = column[String]("LongDescription")

    override def * = (id.?, contractorId, createdBy, rating, shortDescription, longDescription.?) <>
      ((Review.apply _).tupled, Review.unapply)
  }
}
