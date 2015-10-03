package dao

import org.joda.time.DateTime
import scala.concurrent.Future
import javax.inject.{Inject, Singleton}
import models.Upload
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UploadsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  val uploads = TableQuery[Uploads]

  def get(id:Long): Future[Option[Upload]] =
    db.run(uploads.filter(_.id === id).result.headOption)

  def get(uploadType: String, aggregateId: Long): Future[Option[Upload]] =
    db.run(uploads.filter(u => (u.aggregateId === aggregateId) && (u.uploadType === uploadType)).result.headOption)

  def insert(upload:Upload) = {
    val insertion = (uploads returning uploads.map(_.id)) += upload
    val inserted = db.run(insertion)
    inserted.map { resultId =>
      upload.copy(id = Some(resultId))
    }
  }

  def getByUser(userId: Long): Future[Seq[Upload]] =
    db.run(uploads.filter(_.createdBy === userId).result)

  def delete(upload:Upload) = {
    db.run(uploads.filter(_.id === upload.id).delete)
  }

  protected class Uploads(tag: Tag) extends Table[Upload](tag, "Uploads") {
    def id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
    def aggregateId = column[Long]("AggregateId")
    def createdBy = column[Long]("CreatedBy")
    def uploadType = column[String]("Type")
    def physicalLocation = column[String]("PhysicalLocation")
    def createdAt = column[DateTime]("CreatedAt")

    import java.sql.Timestamp
    implicit val dateTime = MappedColumnType.base[DateTime, Timestamp](
        dt => new Timestamp(dt.getMillis), ts => new DateTime(ts.getTime))

    override def * = (id.?, aggregateId, createdBy, uploadType, physicalLocation, createdAt.?) <>
      ((Upload.apply _).tupled, Upload.unapply)
  }
}
