/*
 * wazza-landing-page
 * https://github.com/Wazzaio/wazza-landing-page
 * Copyright (C) 2013-2015  Duarte Barbosa, João Vazão Vasques
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package controllers

import scala.concurrent.Future
import play.api.Play
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.{ WSResponse, WS }
import play.api.libs.json._

object MailChimp {
  private val MailChimpConfigListId = "mailchimp.listId"
  private val MailChimpConfigApiKey = "mailchimp.apiKey"
    
  private val ApiKeyFormat = "[^-]*-(.*)".r
  private val MailChimpApiEndPoint = "https://%s.api.mailchimp.com/2.0/"
  private def MailChimpApi(dataCenter: String): String = MailChimpApiEndPoint.format(dataCenter)
  private val ListSubscribe = "/lists/subscribe.json"

  def subscribe(email: String): Option[Future[WSResponse]] =
    
    for {
      listId <- getStringConfigValue(MailChimpConfigListId)
      apiKey <- getStringConfigValue(MailChimpConfigApiKey)
      listSubscribeMethod <- createListSubscribeMethod(apiKey)
    } yield {
      val params = Json.obj(
          "apikey" -> apiKey,
          "id" -> listId,
		  "double_optin" -> false,
		  "email" -> Json.obj(
		      "email" -> email,
		      "euid" -> email,
		      "leid" -> 123
		  )
	  )

	  WS.url(listSubscribeMethod).post(params)
    }

  private def createListSubscribeMethod(apiKey: String): Option[String] =
    for (ApiKeyFormat(dataCenter) <- ApiKeyFormat.findFirstIn(apiKey))
      yield MailChimpApi(dataCenter) + ListSubscribe

  private def getStringConfigValue(key: String): Option[String] = Play.current.configuration.getString(key)
}