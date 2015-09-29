package dao

import javax.inject.{Inject, Singleton}
import models.{Role, Account}
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.{HasDatabaseConfigProvider, HasDatabaseConfig, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton()
class AccountsDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends AccountsBase with HasDatabaseConfigProvider[JdbcProfile]{
}

trait AccountsBase { self: HasDatabaseConfig[JdbcProfile] =>
  protected val driver: JdbcProfile
  import driver.api._

  val accounts = TableQuery[Accounts]

  def authenticate(email: String, password: String): Future[Option[Account]] = {
    val account = findByEmail(email)
    account.flatMap {
      case None => account
      case Some(user) => if(BCrypt.checkpw(password, user.password)) account else Future.successful(None)
    }
  }

  def findByEmail(email: String): Future[Option[Account]] = db.run(accounts.filter(_.email === email).result.headOption)

  def findById(id: Int): Future[Option[Account]] =
    db.run(accounts.filter(_.id === id).result.headOption)

  def findAll(): Future[Seq[Account]] = db.run(accounts.result)

  def create(account: Account):Future[Account] = {
    val pass = BCrypt.hashpw(account.password, BCrypt.gensalt())
    val insertion = (accounts returning accounts.map(_.id)) += Account(None, account.email, pass, account.role )
    val inserted = db.run(insertion)
    inserted.map { resultId =>
      account.copy(id = Some(resultId))
    }
  }

  protected class Accounts(tag:Tag) extends Table[Account](tag, "Accounts") {
    def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("Email")
    def password = column[String]("Password")
    def role = column[Role]("Role")

    implicit val roleTypeMapper = MappedColumnType.base[Role, String](_.toString(), Role.valueOf(_))

    override def * = (id.?, email, password, role) <> ((Account.apply _).tupled, Account.unapply)
  }
}