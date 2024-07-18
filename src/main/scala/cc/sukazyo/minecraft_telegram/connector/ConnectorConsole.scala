package cc.sukazyo.minecraft_telegram.connector

import cc.sukazyo.cono.morny.system.telegram_api.Standardize.{ChatID, UserID}
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.bot.Bot
import cc.sukazyo.minecraft_telegram.bot.BotExt.Predef.Prefixes
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.request.SendMessage
import net.minecraft.command.CommandSource
import net.minecraft.server.command.{CommandOutput, ServerCommandSource}
import net.minecraft.text.Text
import net.minecraft.util.math.{Vec2f, Vec3d}
import net.minecraft.util.Formatting

object ConnectorConsole extends Predef with Predef.Ext {
	
	val ADMIN_CONSOLE_NAME: String = "Telegram_Admin_Console"
	def getUserAdminConsoleName(user: UserID): String =
		s"Telegram_Admin_u$user"
	
	def getAdminConsole (fromChat: ChatID): ServerCommandSource =
		if fromChat == minecraftLinkedChat then
			linkedTelegramConsole
		else getUserAdminConsole(fromChat)
	
	def isAdminConsole (console: CommandSource): Boolean =
		console == linkedTelegramConsole
	
	def broadcastToTelegramConsole (text: Text, sender: ServerCommandSource): Unit = {
		this.linkedTelegramConsole.sendMessage(
			Text.empty()
				.append(Text.literal(sender.getDisplayName.getString).formatted(Formatting.BOLD))
				.append(Text.literal(": "))
				.append(text)
		)
	}
	
	private lazy val linkedTelegramConsole: ServerCommandSource = this.getLinkedAdminConsole
	
	private def getLinkedAdminConsole: ServerCommandSource =
		ServerCommandSource(
			BotMinecraftCommandOutput(connectorBot, minecraftLinkedChat),
			Vec3d.ZERO, Vec2f.ZERO, null, 4,
			ADMIN_CONSOLE_NAME, Text.literal(ADMIN_CONSOLE_NAME),
			ModMinecraftTelegram.SERVER, null
		)
	
	private def getUserAdminConsole (user: UserID): ServerCommandSource =
		ServerCommandSource(
			BotMinecraftCommandOutput(connectorBot, user),
			Vec3d.ZERO, Vec2f.ZERO, null, 4,
			getUserAdminConsoleName(user), Text.literal(getUserAdminConsoleName(user)),
			ModMinecraftTelegram.SERVER, null
		)
	
	private class BotMinecraftCommandOutput (val bot: Bot, val chat: ChatID) extends CommandOutput {
		import bot.dsl.given
		
		override def sendMessage (message: Text): Unit = {
			
			val plainMessage = message.getString
			bot.runs { () =>
				SendMessage(
					chat,
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
