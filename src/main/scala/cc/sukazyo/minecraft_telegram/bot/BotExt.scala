package cc.sukazyo.minecraft_telegram.bot

import cc.sukazyo.cono.morny.system.telegram_api.Standardize.ChatID
import cc.sukazyo.cono.morny.system.telegram_api.event.EventEnv
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram.logger
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.request.SendMessage
import net.minecraft.server.command.{CommandOutput, ServerCommandSource}
import net.minecraft.text.Text
import net.minecraft.util.math.{Vec2f, Vec3d}

import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute

trait BotExt (using bot: Bot) {
	import bot.dsl.given
	
	object Prefixes {
		
		val SERVER_IDENTITY = "▌"
		val SERVER_MESSAGE = "▌ "
		
	}
	
	def minecraftLinkedChat: ChatID = ModMinecraftTelegram.config.telegram.chat_id
	
	def getBotServerConsole (using event: EventEnv): ServerCommandSource = minecraftAdminCommandSource
	
	private val MC_CONSOLE_NAME = "Telegram_Admin_Console"
	
	private lazy val minecraftAdminCommandSource: ServerCommandSource = ServerCommandSource(
		BotMinecraftCommandOutput,
		Vec3d.ZERO, Vec2f.ZERO, null, 4,
		MC_CONSOLE_NAME, Text.literal(MC_CONSOLE_NAME),
		ModMinecraftTelegram.SERVER, null
	)
	
	private object BotMinecraftCommandOutput extends CommandOutput {
		
		override def sendMessage (message: Text): Unit = {
			
			val plainMessage = message.getString
			logger info message.getString
			
			bot.runs { () =>
				SendMessage(
					minecraftLinkedChat,
					Prefixes.SERVER_MESSAGE + plainMessage
				).entities(MessageEntity(MessageEntity.Type.italic, Prefixes.SERVER_MESSAGE.length, plainMessage.length))
					.unsafeExecute
			}
			
		}
		
		override def shouldReceiveFeedback (): Boolean = true
		
		override def shouldTrackOutput (): Boolean = true
		
		override def shouldBroadcastConsoleToOps (): Boolean = true
		
	}
	
}
