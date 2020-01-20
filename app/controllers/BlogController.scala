package blogController

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class BlogController @Inject() (val controllerComponents: ControllerComponents) extends BaseController{
    def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.blog())
    }
}