package cc.sukazyo.minecraft_telegram.bot.minecraft

import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.request.SendMessage
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.network.message.{MessageType, SignedMessage}
import net.minecraft.server.command.ServerCommandSource
import org.apache.logging.log4j.Logger

class MinecraftCommandMessageListener (using bot: Bot)(using logger: Logger) extends ServerMessageEvents.CommandMessage with BotExt {
	import bot.dsl.given
	
	override def onCommandMessage (message: SignedMessage, source: ServerCommandSource, params: MessageType.Parameters): Unit = {
		bot.runs { () =>
			
			logger debug "received command message"
			val senderName = source.getDisplayName.getString
			val senderTag = s"[$senderName]"
			val plainMessage = message.getContent.getString
			
			SendMessage(
				minecraftLinkedChat,
				s"$senderTag $plainMessage"
			).entities(MessageEntity(MessageEntity.Type.bold, 0, senderTag.length))
				.unsafeExecute
			logger debug "synced command message"
			
		}
	}
	
}
