package activityController

import javax.inject._
import play.api._
import play.api.mvc._
@Singleton
class ActivityController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {
  def index() = Action { implicit request: Request[AnyContent] =>
    println("HOLA")
    Ok(views.html.activity())
  }
}
