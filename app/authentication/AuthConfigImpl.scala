package authentication

import dao.Accounts
import models.Role.{NormalUser, Administrator}
import models.{Role, Account}
import play.api.mvc.RequestHeader
import play.api.mvc.Results._

import scala.concurrent.{Future, ExecutionContext}
import jp.t2v.lab.play2.auth.{TransparentIdContainer, AsyncIdContainer, AuthConfig}
import scala.reflect.{ClassTag, classTag}

trait AuthConfigImpl extends AuthConfig {

  type Id = Int
  type User = Account
  type Authority = Role

  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds = 3600

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = throw new AssertionError("don't use")
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = throw new AssertionError("don't use")
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = throw new AssertionError("don't use")

  def resolveUser(id: Id)(implicit ctx: ExecutionContext) = Accounts.findById(id)
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