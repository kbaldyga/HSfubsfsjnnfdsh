package controllers

import dao.AccountsDao
import jp.t2v.lab.play2.auth.{AsyncIdContainer, AuthConfig, TransparentIdContainer}
import models.Role.{Administrator, NormalUser}
import models.{Account, Role}
import play.api.mvc.RequestHeader
import play.api.mvc.Results._
import play.api.libs.json.Json.toJson

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}

trait AuthConfigImpl extends AuthConfig {
  var accounts: AccountsDao

  type Id = Int
  type User = Account
  type Authority = Role

  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds = 3600

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Ok(toJson("OK")))
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Ok(toJson("OK")))
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = Future.successful(Forbidden("no permission"))

  def resolveUser(id: Id)(implicit ctx: ExecutionContext) =
    accounts.findById(id)
  def authorizationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = throw new AssertionError("don't use")
  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])
                                  (implicit ctx: ExecutionContext) = {
    Future.successful(Forbidden("no permission"))
  }
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext) =
    Future.successful((user.role, authority) match {
      case (Administrator, _) => true
      case (NormalUser, NormalUser) => true
      case _ => false
    })

  override lazy val idContainer: AsyncIdContainer[Id] = AsyncIdContainer(new TransparentIdContainer[Id])
}