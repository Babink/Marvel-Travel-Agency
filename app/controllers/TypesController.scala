package typeController

import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext

import models.Types.{Types, TypesDB, TypesRepo}

@Singleton
class TypesController @Inject() (
    val controllerComponents: ControllerComponents,
    val mTypesRepo: TypesRepo
)(
    implicit ec: ExecutionContext
) extends BaseController
    with play.api.i18n.I18nSupport {
  def index(activityId: Int) = Action { implicit request: Request[AnyContent] =>
    val mAllTypes: Seq[TypesDB] = mTypesRepo.getTypesWithActivityId(activityId)
    Ok(views.html.types(activityId , mAllTypes))
  }
}
