package cc.sukazyo.minecraft_telegram.bot.events

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener}
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Chat.memberHasPermission
import cc.sukazyo.cono.morny.system.telegram_api.formatting.TelegramFormatter.*
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import com.pengrad.telegrambot.model.ChatMember
import com.pengrad.telegrambot.request.SendMessage
import net.minecraft.text.Text
import org.apache.logging.log4j.Logger

class OnMinecraftCommandExecute (using bot: Bot)(using logger: Logger) extends EventListener with BotExt {
	import bot.dsl.given
	
	override def onMessage (using event: EventEnv): Unit = {
		import event.update.message
		
		if message.chat.id != minecraftLinkedChat then return;
		if !(message.text startsWith "//") then return;
		if !message.chat.memberHasPermission(message.from, ChatMember.Status.administrator) then
			SendMessage(
				message.chat.id,
				"403 : Only administrators can execute commands!",
			).replyToMessageId(message.messageId)
				.unsafeExecute
			return;
		
		val mcCmd = ModMinecraftTelegram.SERVER.getCommandManager
		val mcServerConsole = getBotServerConsole
		val command_text = message.text.drop("//".length)
		mcServerConsole.sendMessage(Text.literal(s"Executing admin command by Telegram ${message.from.toLogTag}: /$command_text"))
		mcCmd.executeWithPrefix(mcServerConsole, command_text)
		
	}
	
}
