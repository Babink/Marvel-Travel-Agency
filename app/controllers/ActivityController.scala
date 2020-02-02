package activityController

import javax.inject._
import play.api._
import play.api.mvc._

import models.Activity.{ActivityDB, Activities, ActivityRepo}
import scala.concurrent.ExecutionContext
@Singleton
class ActivityController @Inject() (
    val controllerComponents: ControllerComponents,
    val activityRepo: ActivityRepo
)(implicit ec: ExecutionContext)
    extends BaseController
    with play.api.i18n.I18nSupport {
  def index() = Action { implicit request: Request[AnyContent] =>
    val activities: Seq[ActivityDB] = activityRepo.getAllActivity()
    Ok(views.html.activity(activities, activities.length))
  }
}
