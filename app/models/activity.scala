package models.Activity

import java.sql.Timestamp
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{Tag, ProvenShape}
import slick.jdbc._
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.ControllerComponents
import scala.concurrent.ExecutionContext
import play.api.mvc.AbstractController
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.Future
import play.api.data.Forms._
import play.api.data._
import slick.jdbc.JdbcProfile
import scala.concurrent.Await
import scala.concurrent.duration.`package`.DurationInt

case class ActivityDB(
    id: Int,
    title: String,
    image: String,
    overview: String,
    created: Timestamp
)

case class Activities(
    title: String,
    image: String,
    overview: String
)

object ActivitiesForm {
  val activity_form: Form[Activities] = Form(
    mapping(
      "title" -> nonEmptyText,
      "image" -> nonEmptyText,
      "overview" -> text(minLength = 10, maxLength = 500)
    )(Activities.apply)(Activities.unapply)
  )
}

class ActivityTable(tag: Tag) extends Table[ActivityDB](tag, "Activity") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def image = column[String]("image")
  def overview = column[String]("overview")
  def created =
    column[Timestamp]("created", O.SqlType("timestamp default now()"))

  def * : ProvenShape[ActivityDB] =
    (id, title, image, overview, created) <> ((ActivityDB.apply _).tupled, ActivityDB.unapply)
}

class ActivityRepo @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider,
    cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  val activityRepo = TableQuery[ActivityTable]
  activityRepo.schema.create

  def insert(data: ActivityDB): Int = {
    val insertResult: Future[Int] = db.run(activityRepo += data)
    return Await.result(insertResult, new DurationInt(5).seconds)
  }

  def getAllActivity(): Seq[ActivityDB] = {
    return Await.result(db.run(activityRepo.result), new DurationInt(5).seconds)
  }
}
