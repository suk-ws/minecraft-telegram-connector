package cc.sukazyo.minecraft_telegram.bot.minecraft

import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.request.SendMessage
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.network.message.{MessageType, SignedMessage}
import net.minecraft.server.network.ServerPlayerEntity
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import org.apache.logging.log4j.Logger

class MinecraftChatMessageListener (using bot: Bot)(using logger: Logger) extends ServerMessageEvents.ChatMessage with BotExt {
	import bot.dsl.given
	
	override def onChatMessage (message: SignedMessage, sender: ServerPlayerEntity, params: MessageType.Parameters): Unit = {
		bot.runs { () =>
			
			logger `debug` "received chat message"
			val senderName = sender.getDisplayName.getString
			val senderTag = s"<$senderName>"
			val plainMessage = message.getContent.getString
		
			SendMessage(
				minecraftLinkedChat,
				s"$senderTag $plainMessage"
			).entities(MessageEntity(MessageEntity.Type.bold, 0, senderTag.length))
				.unsafeExecute
			logger `debug` "synced chat message"
			
		}
	}
	
}
