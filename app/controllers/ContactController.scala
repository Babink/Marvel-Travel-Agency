package contactController

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class ContactController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.contact())
  }
}
