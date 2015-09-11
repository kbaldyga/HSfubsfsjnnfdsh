package models

import play.api.libs.json.Json

case class Login(username: String, password: String)
object Login {
  implicit val loginFormat = Json.format[Login]
}

case class Account(id: Option[Int], email: String, password: String, role: Role)

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