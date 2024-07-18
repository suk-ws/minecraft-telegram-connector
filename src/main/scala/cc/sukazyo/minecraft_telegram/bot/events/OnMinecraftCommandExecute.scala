package cc.sukazyo.minecraft_telegram.bot.events

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener}
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Chat.memberHasPermission
import cc.sukazyo.cono.morny.system.telegram_api.formatting.TelegramFormatter.*
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.LimboChat
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.connector.ConnectorConsole
import com.pengrad.telegrambot.model.ChatMember
import com.pengrad.telegrambot.request.SendMessage
import net.minecraft.text.Text
import org.apache.logging.log4j.Logger

class OnMinecraftCommandExecute (using bot: Bot)(using logger: Logger) extends EventListener with BotExt {
	import bot.dsl.given
	
	override def onMessage (using event: EventEnv): Unit = {
		import event.update.message
		
		// todo: can be configured to if accept the non-linked-chat command
//		if message.chat.id != minecraftLinkedChat then return;
		if message.text == null then return;
		if !(message.text `startsWith` "//") then return;
		
		if !LimboChat(minecraftLinkedChat).memberHasPermission(message.from, ChatMember.Status.administrator) then
			SendMessage(
				message.chat.id,
				"403 : Only administrators can execute commands!",
			).replyToMessageId(message.messageId)
				.unsafeExecute
			event.setEventOk
			return;
		
		val mcCmd = ModMinecraftTelegram.SERVER.getCommandManager
		val mcServerConsole = ConnectorConsole.getAdminConsole(message.chat.id)
		val command_text = message.text.drop("//".length)
		val log_executing = Text.literal(
			s"Executing admin command by Telegram ${message.from.toLogTag} in chat ${message.chat.safe_name}: /$command_text"
		)
		logger `info` log_executing.getString
		mcServerConsole.sendMessage(log_executing)
		mcCmd.executeWithPrefix(mcServerConsole, command_text)
		event.setEventOk
		
	}
	
}
