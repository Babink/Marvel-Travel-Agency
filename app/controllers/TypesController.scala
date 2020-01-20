package typeController

import javax.inject._
import play.api._
import play.api.mvc._
@Singleton
class TypesController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.types())
  }
}
