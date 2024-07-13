package cc.sukazyo.minecraft_telegram.bot

import cc.sukazyo.cono.morny.system.telegram_api.event.EventRuntimeException
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import cc.sukazyo.minecraft_telegram.bot.BotConfig.{IncorrectBotAccountException, LoginFailedException}
import cc.sukazyo.minecraft_telegram.config.ProxyConfig
import cc.sukazyo.minecraft_telegram.utils.Log4jExtension.LoggerExt
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.User
import com.pengrad.telegrambot.request.GetMe
import com.pengrad.telegrambot.response.GetMeResponse
import okhttp3.OkHttpClient
import org.apache.logging.log4j.Logger

import scala.util.boundary

case class BotConfig (
	
	api_server: Option[BotApiServer],
	
	proxy: Option[ProxyConfig],
	
	username: Option[String],
	token: String,
	
) {
	
	def getBot: TelegramBot = {
		
		val builder = TelegramBot.Builder(this.token)
		val okHttpClientBuilder = OkHttpClient.Builder()
		
		this.api_server match
			case Some(api_server) =>
				builder.apiUrl(api_server.api_url)
				builder.fileApiUrl(api_server.file_api_url)
				builder.useTestServer(api_server.use_test_server)
			case None =>
		
		this.proxy match
			case None =>
			case Some(proxy) =>
				okHttpClientBuilder.proxy(proxy.getProxy)
		
		builder.okHttpClient(okHttpClientBuilder.build())
		builder.build
		
	}
	
	@throws[LoginFailedException]
	def loginToBot (using logger: Logger): (TelegramBot, User) = {
		
		given bot: TelegramBot = getBot
		
		val me = boundary {
			
			for (i <- 1 to 3) {
				try {
					logger info s"Trying to login to bot... ($i/3)"
					val aboutMe: GetMeResponse = GetMe().unsafeExecute
					boundary break Right(aboutMe.user)
				} catch case e: EventRuntimeException =>
					logger error s"Failed to login to bot:"
					logger errorExceptionSimple e
					logger warn "Waiting for next try..."
					Thread sleep 1000
			}
			
			Left(LoginFailedException())
			
		}
		
		me match
			case Left(e) => throw e
			case Right(me) =>
				
				if me.username() != this.username.orNull then
					throw IncorrectBotAccountException()
				
				(bot, me)
				
		
	}
	
}

object BotConfig {
	
	class LoginFailedException extends Exception
	class IncorrectBotAccountException extends LoginFailedException
	
}
