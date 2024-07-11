package cc.sukazyo.minecraft_telegram.bot

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.User
import org.apache.logging.log4j.Logger

class Bot (config: BotConfig)(using logger: Logger) {
	
	val (
		bot: TelegramBot,
		bot_user: User
	) = config.loginToBot
	
	logger info s"Logged in to bot successfully : bot @${bot_user.username}[${bot_user.id}]"
	
}
