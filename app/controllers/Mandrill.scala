package controllers

import scala.concurrent.Future
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws.{ Response, WS }
import play.api.libs.json._

object Mandrill {

	private val mandrillApiKey = "mandrill.apiKey"
	private val mandrillApiEndpoint = "https://mandrillapp.com/api/1.0/"

	private def generateEndpoint(module: String, action: String) : String = {
		return mandrillApiEndpoint + module + "/" + action + ".json"
	}

	def sendEmail(email: String): Future[Response] = {
		val params = Json.obj(
			"key" -> getStringConfigValue(mandrillApiKey),
			"message" -> Json.obj(
				"html" -> ("<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'><html lang='en'><head>	<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>	<meta name='viewport' content='initial-scale=1.0'> <!-- So that mobile webkit will display zoomed in -->	<title>Wazza - Thanks for signing up!</title> <!-- the <title> tag shows on email notifications on Android 4.4.  -->	<style type='text/css'>		/* Force Hotmail to display emails at full width */		.ReadMsgBody {			width: 100%;			background-color: #eeeeee;		}		/* Force Hotmail to display emails at full width */		.ExternalClass {			width: 100%;			background-color: #eeeeee;		}		/* Forces Hotmail to display normal line spacing. */		.ExternalClass,		.ExternalClass p,		.ExternalClass span,		.ExternalClass font,		.ExternalClass td,		.ExternalClass div {			line-height:100%;		}		/* Resolves webkit padding issue. */		table {			border-spacing:0;		}		/* Resolves the Outlook 2007, 2010, and Gmail td padding issue. */		table td {			border-collapse:collapse;		}		/* Yahoo auto-sensing link color and border */		.yshortcuts a {			border-bottom: none !important;		}		/* Styling the tel URL scheme */		a[href^=tel]{			color:#000000 !important;		}		a {			color:#58b094;		}		/* Media queries for when the viewport is smaller than the default email width but not too narrow. */		@@media screen and (max-device-width: 650px), screen and (max-width: 650px) {			/* Constrain email width for small screens */			table[class='email-container'] {				width: 100% !important;			}			/* Constrain tables for small screens */			table[class='fluid'] {				width: 100% !important;			}			/* Force images to resize to full width of their container */			img[class='fluid'],			img[class='force-col-center'] {				width: 100% !important;				max-width: 100% !important;				height: auto !important;			}			/* And center these ones */			img[class='force-col-center'] {				margin: auto !important;			}			/* Force table cells into rows */			td[class='force-col'],			td[class='force-col-center'] {				display: block !important;				width: 100% !important;				clear: both;			}			/* And center these ones */			td[class='force-col-center'] {				text-align: center !important;			}			/* Force table cells into rows */			/* Float a previously stacked image to the left */			img[class='col-3-img-l'] {				float: left;				margin: 0 15px 15px 0;			}			/* Float a previously stacked image to the right */			img[class='col-3-img-r'] {				float: right;				margin: 0 0 15px 15px;			}			/* Make bulletproof buttons full width */			table[class='button'] {				width: 100% !important;			}		}		/* Media queries for when the viewport is narrow. */		/* Rules prefixed with 'hh-' (for 'handheld') repeat much of what's above, but these don't trigger until the smaller screen width. */		@@media screen and (max-device-width: 425px), screen and (max-width: 425px) {			/* Helper only visible on handhelds. All styles are inline along with a `display:none`, which this class overrides */			div[class='hh-visible'] {				display: block !important;			}			/* Center stuff */			div[class='hh-center'] {				text-align: center;				width: 100% !important;			}			/* Constrain tables for small screens */			table[class='hh-fluid'] {				width: 100% !important;			}			/* Force images to resize to full width of their container */			img[class='hh-fluid'],			img[class='hh-force-col-center'] {				width: 100% !important;				max-width: 100% !important;				height: auto !important;			}			/* And center these ones */			img[class='hh-force-col-center'] {				margin: auto !important;			}			/* Force table cells into rows */			td[class='hh-force-col'],			td[class='hh-force-col-center'] {				display: block !important;				width: 100% !important;				clear: both;			}			/* And center these ones */			td[class='hh-force-col-center'] {				text-align: center !important;			}			/* Stack the previously floated images */			img[class='col-3-img-l'],			img[class='col-3-img-r'] {				float: none !important;				margin: 15px auto !important;				text-align: center !important;			}		}	</style></head><body bgcolor='#f4f4f4' leftmargin='0' topmargin='0' marginwidth='0' marginheight='0' style='margin:0; padding:0;-webkit-text-size-adjust:none; -ms-text-size-adjust:none;background: #f4f4f4;'>	<table cellpadding='0' cellspacing='0' border='0' height='100%' width='100%' bgcolor='#f4f4f4' style='background: #f4f4f4;'><tr><td height='30' style='font-size: 0; line-height: 0;'>&nbsp;</td></tr><tr><td>		<!-- Email Container : BEGIN -->		<table border='0' width='600' cellpadding='0' cellspacing='0' align='center' style='background: #ffffff;border: 1px solid #e5e5e5;' class='email-container'>			<tr><td style='border-bottom: 1px solid #e5e5e5;'>				<table border='0' width='600' cellpadding='0' cellspacing='0' align='center' class='email-container'>					<tr>						<td valign='middle' style='padding: 30px;text-align: left; background: #34495e;'>							<img src='http://www.wazza.io/assets/images/wazza_logo_white2.png' alt='Wazza' border='0' width='250'>						</td>					</tr>				</table>				<!-- Welcome Email Content: BEGIN -->				<table border='0' width='100%' cellpadding='0' cellspacing='0' align='center'>					<tr>						<td style='padding: 0px 30px; font-family: sans-serif; font-size: 24px; line-height: 28px; color: #34495e;'>							<br>							Welcome!						</td>					</tr>					<tr>						<td style='padding: 0 30px; font-family: sans-serif; font-size: 20px; line-height: 22px; color: #999;'>							You have requested an invite for trying Wazza.						</td>					</tr>					<tr><td height='30' style='font-size: 0; line-height: 0;'>&nbsp;</td></tr>					<tr>						<td style='padding: 0px 30px; font-family: sans-serif; font-size: 14px; line-height: 18px; color: #666;'>							<p>At Wazza, we are delighted that you have chosen to know more about us. </p>							Our mission is to help mobile companies increase in-app purchase revenue in a smarter way through advanced Analytics.							We are currently in private beta and we would like to have you onboard.  							If you have a promocode, head over to the <a href='http://www.wazza.io/register'>register</a> page and we will keep you updated. Otherwise, we will notify you once another batch becomes available.  							<p>We love talking to customers. Feel free to contact us anytime.</p> 							<p>Follow us on								<a href='https://angel.co/wazza'>AngelList</a>,								<a href='https://www.linkedin.com/company/wazza-mobile'>LinkedIn</a>,								<a href='https://twitter.com/wazza_io'>Twitter</a>,								and <a href='https://www.facebook.com/wazzaio'>Facebook</a>								to get our latest news!</p>								<br>							</td>						</tr>						<tr><td height='30' style='font-size: 0; line-height: 0;'>&nbsp;</td></tr>						<tr>							<td style='padding: 0px 30px; font-family: sans-serif; font-size: 14px; line-height: 18px; color: #666;'>								The Wazza team.							</td>						</tr>						<tr><td height='30' style='font-size: 0; line-height: 0;'>&nbsp;</td></tr>					</table>					<!-- Password Email Content: END -->				</td>			</tr>		</table>		<table border='0' width='100%' cellpadding='0' cellspacing='0' align='center' class='email-container'>			<tr>				<td style='text-align: center;padding: 20px;font-family: sans-serif; font-size: 12px; line-height: 18px;color: #888888;'>					For support please contact <a href='mailto:support@@wazza.io' style='text-decoration:none;'>support@wazza.io</a>.<br>					&copy; 2014 Wazza.				</td>			</tr>		</table>			</td></tr></table></body>"
				),
				"subject" -> "Welcome to Wazza!",
				"from_email" -> "hello@wazza.io",
				"from_name" -> "Wazza",
				"to" -> Json.arr(
					Json.obj(
						"email" -> email,
						"type" -> "to"
					)
				)
			)
		)

		WS.url(generateEndpoint("messages", "send")).post(params)
	}

	def sendNotification(email: String): Future[Response] = {
		val params = Json.obj(
			"key" -> getStringConfigValue(mandrillApiKey),
			"message" -> Json.obj(
				"text" -> ("A new user with email " + email + " has signed up"),
				"subject" -> "New Sign up",
				"from_email" -> "no-reply@wazza.io",
				"from_name" -> "Wazza",
				"to" -> Json.arr(
					Json.obj(
						"email" -> "joao@wazza.io",
						"type" -> "to"
					),
					Json.obj(
						"email" -> "duarte@wazza.io",
						"type" -> "to"
					)
				)
			)
		)

		WS.url(generateEndpoint("messages", "send")).post(params)
	}

	def sendRegisterNotification(name: String, email: String, company: String): Future[Response] = {
		val params = Json.obj(
			"key" -> getStringConfigValue(mandrillApiKey),
			"message" -> Json.obj(
				"text" -> (name + " with email " + email + " from " + company + "has registered"),
				"subject" -> "New registration",
				"from_email" -> "no-reply@wazza.io",
				"from_name" -> "Wazza",
				"to" -> Json.arr(
					Json.obj(
						"email" -> "joao@wazza.io",
						"type" -> "to"
					),
					Json.obj(
						"email" -> "duarte@wazza.io",
						"type" -> "to"
					)
				)
			)
		)

		WS.url(generateEndpoint("messages", "send")).post(params)
	}

	private def getStringConfigValue(key: String): Option[String] = Play.current.configuration.getString(key)
}
