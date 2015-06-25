package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.Logger
import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.db.DB
import play.api.Play.current
import anorm._
import play.api.libs.concurrent.Execution.Implicits._

  case class User(
    name: String,
    email: String,
    company: String,
    promocode: Option[String],
    country: String
  )

object Application extends Controller {
  def isValid(email: String): Boolean = {
    if("""(?=[^\s]+)(?=(\w+)@([\w\.]+))""".r.findFirstIn(email) == None) {
      false
    } else {
      ! DatabaseService.exists(email)
    }
  }

  def index = Action {
    Ok(views.html.index())
  }

  def payment_provider = Action {
    Ok(views.html.index_pp())
  }

  def mobile_developer = Action {
    Ok(views.html.index_app())
  }

  def register = Action {
    Ok(views.html.register())
  }

  def docs = Action {
    Ok(views.html.documentation())
  }

  def demo = Action {
    Ok(views.html.demo())
  }

  val registrationForm: Form[User] = Form(
      mapping(
        "name" -> nonEmptyText,
        "email" -> nonEmptyText,
        "company" -> nonEmptyText,
        "promocode" -> optional(text),
        "country" -> nonEmptyText
      )(User.apply)(User.unapply)
  )

  def registerSubmit = Action{ implicit request =>
    registrationForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.index())
      },
      user => {
        if(isValid(user.email)){
          println("valid email")
          DatabaseService.save(user.email)
          DatabaseService.registration(user)
          Ok
        } else {
          BadRequest
        }
      }
    )
  }
}

object DatabaseService {

  private def init() {
    DB.withConnection { implicit conn =>
      val query = "CREATE TABLE IF NOT EXISTS Contacts(user_id SERIAL NOT NULL PRIMARY KEY,email varchar(225) NOT NULL UNIQUE)"
      SQL(query).execute
    }
  }

  def getAllContacts() : Unit = {
    DB.withConnection { implicit conn =>
      val selectedContacts = SQL("Select email from Contacts")
      println(selectedContacts.toString)
    }
  }

  def exists(email: String) : Boolean = {
    init
    DB.withConnection { implicit conn =>
      SQL("Select 1 from Contacts Where email='" + email + "'").execute();
    }
  }

  def save(email: String) : Unit = {
    init
    DB.withConnection { implicit conn =>
      val id: Option[Long] = SQL("insert into Contacts(email) values ({email})")
              .on('email -> email).executeInsert()
      sendNotificationEmail(email)
    }
  }

  private def sendNotificationEmail(emailContact: String) : Unit = {
    val response = Mandrill.sendEmail(emailContact)
    println("Email to user")
    response.map({
      response => println(response.body)
    })

    val notification = Mandrill.sendNotification(emailContact)
    println("Notification email")
    notification.map({
      notification => println(notification.body)
    })
  }

  def registration(user: User) : Unit = {
    val notification = Mandrill.sendRegisterNotification(user.name, user.email, user.company, user.promocode)
    println("Notification email - registration")
    notification.map({
      notification => println(notification.body)
    })
  }
}
