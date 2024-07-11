package cc.sukazyo.minecraft_telegram.utils.telegram_api

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.*
import com.pengrad.telegrambot.request.{BaseRequest, GetChatMember}
import com.pengrad.telegrambot.response.BaseResponse

import scala.annotation.targetName

object TelegramExtensions {
	
	object Bot {
		extension (bot: TelegramBot) {
			
			@throws[EventRuntimeException]
			def exec[T <: BaseRequest[T, R], R <: BaseResponse] (request: T, onError_message: String = ""): R = {
				try {
					val response = bot execute request
					if response.isOk then return response
					throw EventRuntimeException.ActionFailed(
						if onError_message.isEmpty then response.errorCode.toString else onError_message,
						response
					)
				} catch
					case e: EventRuntimeException.ActionFailed => throw e
					case e: RuntimeException =>
						throw EventRuntimeException.ClientFailed(e)
			}
			
		}
	}
	
}