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

  def authenticate(token: String, password: String): Future[Option[Account]] = {
    val account = findByToken(token)
    account.flatMap {
      case None => account
      case Some(user) => if(BCrypt.checkpw(password, user.password)) account else Future.successful(None)
    }
  }

  def findByToken(token: String): Future[Option[Account]] =
    db.run(accounts.filter(a => (a.login === token) || (a.email === token)).result.headOption)

  def findByEmail(email: String): Future[Option[Account]] = db.run(accounts.filter(_.email === email).result.headOption)

  def findById(id: Int): Future[Option[Account]] =
    db.run(accounts.filter(_.id === id).result.headOption)

  def findAll(): Future[Seq[Account]] = db.run(accounts.result)

  def create(account: Account):Future[Account] = {
    val pass = BCrypt.hashpw(account.password, BCrypt.gensalt())
    val insertion = (accounts returning accounts.map(_.id)) +=
      Account(None, account.login, account.email, pass, account.displayName, account.registeredFromIp, account.role )
    val inserted = db.run(insertion)
    inserted.map { resultId =>
      account.copy(id = Some(resultId))
    }
  }

  protected class Accounts(tag:Tag) extends Table[Account](tag, "Accounts") {
    def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)
    def login = column[String]("Login")
    def email = column[String]("Email")
    def password = column[String]("Password")
    def displayName = column[String]("DisplayName")
    def registeredFromIp = column[String]("RegisteredFromIp")
    def role = column[Role]("Role")

    implicit val roleTypeMapper = MappedColumnType.base[Role, String](_.toString(), Role.valueOf(_))

    override def * = (id.?, login, email, password, displayName, registeredFromIp, role) <>
      ((Account.apply _).tupled, Account.unapply)
  }
}