package models

import play.api.libs.json.Json

case class Login(username: String, password: String)
object Login {
  implicit val loginFormat = Json.format[Login]
}

case class Account(id: Option[Int], email: String, password: String, role: Role)

// TODO: Implement dao
// TODO: Implement BCrypt
//object Account {
//
//  def authenticate(email: String, password: String): Option[Account] =  ???
//
//  def findByEmail(email: String): Option[Account] = ???
//
//  def findById(id: Int): Option[Account] = Some(Account(100, "username", "password", NormalUser))
//
//  def findAll(): Seq[Account] = ???
//
//  def create(account: Account) = ???
//
//}

//
//object Account extends SQLSyntaxSupport[Account] {
//
//  private val a = syntax("a")
//
//  def apply(a: SyntaxProvider[Account])(rs: WrappedResultSet): Account = autoConstruct(rs, a)
//
//  private val auto = AutoSession
//
//  def authenticate(email: String, password: String)(implicit s: DBSession = auto): Option[Account] = {
//    findByEmail(email).filter { account => BCrypt.checkpw(password, account.password) }
//  }
//
//  def findByEmail(email: String)(implicit s: DBSession = auto): Option[Account] = withSQL {
//    select.from(Account as a).where.eq(a.email, email)
//  }.map(Account(a)).single.apply()
//
//  def findById(id: Int)(implicit s: DBSession = auto): Option[Account] = withSQL {
//    select.from(Account as a).where.eq(a.id, id)
//  }.map(Account(a)).single.apply()
//
//  def findAll()(implicit s: DBSession = auto): Seq[Account] = withSQL {
//    select.from(Account as a)
//  }.map(Account(a)).list.apply()
//
//  def create(account: Account)(implicit s: DBSession = auto) {
//    withSQL {
//      import account._
//      val pass = BCrypt.hashpw(account.password, BCrypt.gensalt())
//      insert.into(Account).values(id, email, pass, name, role.toString)
//    }.update.apply()
//  }
//
//}

sealed trait Role
object Role {
  case object Administrator extends Role
  case object NormalUser extends Role
  def valueOf(value: String): Role = value match {
    case "Administrator" => Administrator
    case "NormalUser"    => NormalUser
    case _ => throw new IllegalArgumentException()
  }
}