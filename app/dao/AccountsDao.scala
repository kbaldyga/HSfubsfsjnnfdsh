package dao

import models.{Role, Account}
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object Accounts extends AccountsDaoTrait with HasDatabaseConfig[JdbcProfile]{
  override protected val dbConfig: DatabaseConfig[JdbcProfile] =
    DatabaseConfigProvider.get[JdbcProfile](Play.current)
}

trait AccountsDaoTrait { self: HasDatabaseConfig[JdbcProfile] =>
  protected val driver: JdbcProfile
  import driver.api._

  val accounts = TableQuery[Accounts]

  def authenticate(email: String, password: String): Future[Option[Account]] =
    db.run(accounts.filter(_.email === email).filter(_.password === password).result.headOption)

  def findByEmail(email: String): Future[Option[Account]] = db.run(accounts.filter(_.email === email).result.headOption)

  def findById(id: Int): Future[Option[Account]] =
    db.run(accounts.filter(_.id === id).result.headOption)

  def findAll(): Future[Seq[Account]] = db.run(accounts.result)

  def create(account: Account) = {
    val insertion = (accounts returning accounts.map(_.id)) += account
    val inserted = db.run(insertion)
    inserted.map { resultId =>
      account.copy(id = Some(resultId))
    }
  }

  class Accounts(tag:Tag) extends Table[Account](tag, "Accounts") {
    def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("Email")
    def password = column[String]("Password")
    def role = column[Role]("Role")

    implicit val roleTypeMapper = MappedColumnType.base[Role, String](_.toString(), Role.valueOf(_))

    override def * = (id.?, email, password, role) <> ((Account.apply _).tupled, Account.unapply)
  }
}