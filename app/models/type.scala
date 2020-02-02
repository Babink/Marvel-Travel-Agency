package models.Types

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
import models.Activity.ActivityTable

case class Types(
    title: String,
    activity: String,
    image_one: String,
    image_two: String,
    overview: String
)

case class TypesDB(
    id: Int,
    title: String,
    activity_id: Int,
    image_one: String,
    image_two: String,
    overview: String,
    created: Timestamp
)

object TypesForm {
  val types_form: Form[Types] = Form(
    mapping(
      "title" -> nonEmptyText,
      "activity" -> nonEmptyText,
      "image_one" -> nonEmptyText,
      "image_two" -> nonEmptyText,
      "overview" -> nonEmptyText
    )(Types.apply)(Types.unapply)
  )
}

class TypesTable(tag: Tag) extends Table[TypesDB](tag, "Types") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def activity_id = column[Int]("activity_id")
  def image_one = column[String]("image_one")
  def image_two = column[String]("image_two")
  def overview = column[String]("overview")
  def created =
    column[Timestamp]("created", O.SqlType("timestamp default now()"))

  def * : ProvenShape[TypesDB] =
    (id, title, activity_id, image_one, image_two, overview, created) <> ((TypesDB.apply _).tupled, TypesDB.unapply)

  def activity =
    foreignKey("activity_fk", activity_id, TableQuery[ActivityTable])(
      _.id,
      onDelete = ForeignKeyAction.Cascade
    )
}

class TypesRepo @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider,
    cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  val typeRepo: TableQuery[TypesTable] = TableQuery[TypesTable]
  typeRepo.schema.create

  def insert(data: TypesDB): Int = {
    val insertResult: Future[Int] = db.run(typeRepo += data)
    return Await.result(insertResult, new DurationInt(10).seconds)
  }

  def getAllTypes(): Seq[TypesDB] = {
    val dbResult: Future[Seq[TypesDB]] = db.run(typeRepo.result)
    return Await.result(dbResult, new DurationInt(10).seconds)
  }

  def getTypesWithActivityId(activityId: Int): Seq[TypesDB] = {
    val dbResult: Future[Seq[TypesDB]] =
      db.run(typeRepo.filter(_.activity_id === activityId).result)

    return Await.result(dbResult, new DurationInt(10).seconds)
  }
}
