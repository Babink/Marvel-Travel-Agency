package adminController

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import java.sql.Timestamp
import java.util.Date

import java.util._;
import scala.util.{Success, Failure}
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._

import java.security.MessageDigest
import scala.concurrent.ExecutionContext
import models.Activity.{Activities, ActivitiesForm, ActivityRepo, ActivityDB}
import models.Types.{Types, TypesForm, TypesDB, TypesRepo}
import models.Trip.{Trips, TripForm, TripDB, TripRepo}
@Singleton
class AdminController @Inject() (
    val mActivityRepo: ActivityRepo,
    val typesRepo: TypesRepo,
    val tripRepo: TripRepo,
    val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BaseController
    with play.api.i18n.I18nSupport {

  def activity() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.adminActivity(ActivitiesForm.activity_form, 0))
  }

  def activity_post() = Action { implicit request: Request[AnyContent] =>
    ActivitiesForm.activity_form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.adminActivity(formWithErrors, 0))
      },
      (docs: Activities) => {
        val result: Int = add_activity(docs)

        if (result == 1) {
          Ok(
            views.html.adminActivity(ActivitiesForm.activity_form, result)
          )
        } else {
          Ok(
            views.html.adminActivity(ActivitiesForm.activity_form, 2)
          )
        }
      }
    )
  }

  def add_activity(docs: Activities): Int = {
    val mDate: Date = new Date()
    val mActivity: ActivityDB = ActivityDB(
      1,
      docs.title,
      docs.image,
      docs.overview,
      new Timestamp(mDate.getDate())
    )
    return mActivityRepo.insert(mActivity)
  }

  def getAllActivityForDropDown(): Seq[(String, String)] = {
    val activities: Seq[ActivityDB] = mActivityRepo.getAllActivity()
    val newVal: Seq[(String, String)] =
      activities.map((docs: ActivityDB) => (docs.id.toString(), docs.title))

    return newVal
  }

  def getAllTypesForDropDown(): Seq[(String, String)] = {
    val types: Seq[TypesDB] = typesRepo.getAllTypes()
    val newVal: Seq[(String, String)] =
      types.map((docs: TypesDB) => (docs.id.toString(), docs.title))

    return newVal
  }

  def types() = Action { implicit request: Request[AnyContent] =>
    val mSeq: Seq[(String, String)] = getAllActivityForDropDown();

    Ok(views.html.adminType(TypesForm.types_form, 0, mSeq))
  }

  def types_post() = Action { implicit request: Request[AnyContent] =>
    val mList: Seq[(String, String)] = getAllActivityForDropDown();

    TypesForm.types_form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.adminType(formWithErrors, 0, mList))
      },
      (docs: Types) => {
        val result: Int = add_types(docs)

        if (result == 1) {
          Ok(views.html.adminType(TypesForm.types_form, 1, mList))
        } else {
          Ok(views.html.adminType(TypesForm.types_form, 2, mList))
        }
      }
    )
  }

  def add_types(docs: Types): Int = {
    val mDate: Date = new Date()

    val mTypes: TypesDB = TypesDB(
      1,
      docs.title,
      docs.activity.toInt,
      docs.image_one,
      docs.image_two,
      docs.overview,
      new Timestamp(mDate.getDate())
    )

    return typesRepo.insert(mTypes)
  }

  def trip() = Action { implicit request: Request[AnyContent] =>
    val allTypes: Seq[(String, String)] = getAllTypesForDropDown()
    Ok(views.html.adminTrip(TripForm.trip_form, 0 , allTypes))
  }

  def trip_post() = Action { implicit request: Request[AnyContent] =>
    val allTypes: Seq[(String, String)] = getAllTypesForDropDown()

    TripForm.trip_form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.adminTrip(formWithErrors, 0 , allTypes))
      },
      (docs: Trips) => {
        val result: Int = add_trip(docs)

        if (result == 1) {
          Ok(views.html.adminTrip(TripForm.trip_form, 1 , allTypes))
        } else {
          Ok(views.html.adminTrip(TripForm.trip_form, 2 , allTypes))
        }
      }
    )
  }

  def add_trip(docs: Trips): Int = {
    val date: Date = new Date()
    val tripData: TripDB = TripDB(
      1,
      docs.title,
      docs.overview,
      docs.cost,
      docs.no_of_days,
      docs.types.toInt,
      docs.image_one,
      docs.image_two,
      docs.image_three,
      docs.image_four,
      docs.image_five,
      new Timestamp(date.getDate())
    )
    return tripRepo.insert(tripData)
  }
}
