package cc.sukazyo.minecraft_telegram.bot.minecraft

import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.request.SendMessage
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text
import org.apache.logging.log4j.Logger

class MinecraftGameMessageListener (using bot: Bot)(using logger: Logger) extends ServerMessageEvents.GameMessage with BotExt {
	import bot.dsl.given
	
	override def onGameMessage (server: MinecraftServer, message: Text, overlay: Boolean): Unit = {
		bot.runs { () =>
			
			SendMessage(
				minecraftLinkedChat,
				s"""${Prefixes.SERVER_MESSAGE}${message.getString}"""
			).entities(MessageEntity(MessageEntity.Type.bold, 0, 1))
				.unsafeExecute
			logger `debug` "synced game message to telegram"
			
		}
	}
	
}
