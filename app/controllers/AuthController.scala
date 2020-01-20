package authController

import javax.inject._
import play.api._
import play.api.mvc._

class AuthController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {

  // Login
  def signin() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signin())
  }

//   Register Account
  def signup() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup())
  }
}
