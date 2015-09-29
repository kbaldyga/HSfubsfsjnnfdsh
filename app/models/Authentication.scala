package models

import play.api.libs.json.Json

case class Register(username: String, password: String, email: String, displayName:String)
object Register {
  implicit val format = Json.format[Register]
}

case class Login(token: String, password: String)
object Login { implicit val format = Json.format[Login]}

case class Account(id: Option[Int], login:String, email: String,
                   password: String, displayName: String,
                   registeredFromIp:String, role: Role)

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