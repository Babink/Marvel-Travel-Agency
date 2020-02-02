package models.Trip

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

import models.Types.TypesTable

case class Trips(
    title: String,
    overview: String,
    cost: Int,
    no_of_days: Int,
    types: String,
    image_one: String,
    image_two: String,
    image_three: String,
    image_four: String,
    image_five: String
)

case class TripDB(
    id: Int,
    title: String,
    overview: String,
    cost: Int,
    no_of_days: Int,
    types_id: Int,
    image_one: String,
    image_two: String,
    image_three: String,
    image_four: String,
    image_five: String,
    created: Timestamp
)

object TripForm {
  val trip_form: Form[Trips] = Form(
    mapping(
      "title" -> nonEmptyText,
      "overview" -> nonEmptyText,
      "cost" -> number,
      "no_of_days" -> number,
      "types" -> nonEmptyText,
      "image_one" -> nonEmptyText,
      "image_two" -> nonEmptyText,
      "image_three" -> nonEmptyText,
      "image_four" -> nonEmptyText,
      "image_five" -> nonEmptyText
    )(Trips.apply)(Trips.unapply)
  )
}

class TripTable(tag: Tag) extends Table[TripDB](tag, "Trips") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def overview = column[String]("overview")
  def cost = column[Int]("cost")
  def no_of_days = column[Int]("no_of_days")
  def types_id = column[Int]("types_id")
  def image_one = column[String]("image_one")
  def image_two = column[String]("image_two")
  def image_three = column[String]("image_three")
  def image_four = column[String]("image_four")
  def image_five = column[String]("image_five")
  def created = column[Timestamp]("created")

  override def * : ProvenShape[TripDB] =
    (
      id,
      title,
      overview,
      cost,
      no_of_days,
      types_id,
      image_one,
      image_two,
      image_three,
      image_four,
      image_five,
      created
    ) <> ((TripDB.apply _).tupled, TripDB.unapply)

  def types = foreignKey("type_fk", types_id, TableQuery[TypesTable])(
    _.id,
    onDelete = ForeignKeyAction.Cascade
  )
}

class TripRepo @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider,
    cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  val tripRepo: TableQuery[TripTable] = TableQuery[TripTable]
  tripRepo.schema.create

  def insert(data: TripDB): Int = {
    val addResult: Future[Int] = db.run(tripRepo += data)
    return Await.result(addResult, new DurationInt(5).seconds)
  }

  def getAllTrip(): Seq[TripDB] = {
    val tripResult: Future[Seq[TripDB]] = db.run(tripRepo.result)
    return Await.result(tripResult, new DurationInt(5).seconds)
  }
}
