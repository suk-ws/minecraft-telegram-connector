package cc.sukazyo.minecraft_telegram.bot

import cc.sukazyo.minecraft_telegram.bot.BotConfig.LoginFailedException
import cc.sukazyo.minecraft_telegram.utils.telegram_api.EventRuntimeException
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.GetMe
import org.apache.logging.log4j.Logger

import scala.util.boundary
import cc.sukazyo.minecraft_telegram.utils.telegram_api.TelegramExtensions.Bot.*
import com.pengrad.telegrambot.model.User
import com.pengrad.telegrambot.response.GetMeResponse

case class BotConfig (
	
	api_server: Option[BotApiServer],
	
	username: Option[String],
	token: String,
	
) {
	
	def getBot: TelegramBot = {
		
		val builder = TelegramBot.Builder(this.token)
		
		this.api_server match
			case Some(api_server) =>
				builder.apiUrl(api_server.api_url)
				builder.fileApiUrl(api_server.file_api_url)
				builder.useTestServer(api_server.use_test_server)
			case None =>
		
		builder.build
		
	}
	
	@throws[LoginFailedException]
	def loginToBot (using logger: Logger): (TelegramBot, User) = {
		
		val bot = getBot
		
		val me = boundary {
			
			for (i <- 1 to 3) {
				try {
					
					logger info s"Trying to login to bot... ($i/3)"
					val aboutMe: GetMeResponse = bot exec GetMe()
					
					boundary break aboutMe.user
					
				} catch case e: EventRuntimeException =>
					logger error s"Failed to login to bot:"
					logger error e
					logger warn "Waiting for next try..."
					Thread sleep 1000
			}
			throw LoginFailedException()
		}
		
		(bot, me)
		
	}
	
}

object BotConfig {
	
	class LoginFailedException extends Exception
	
}
