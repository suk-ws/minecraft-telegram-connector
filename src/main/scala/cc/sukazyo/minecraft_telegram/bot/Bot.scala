package cc.sukazyo.minecraft_telegram.bot

import cc.sukazyo.minecraft_telegram.bot.events.{BotIgnoringOutdatedMessage, OnMinecraftCommandExecute, OnTelegram2Minecraft}
import cc.sukazyo.minecraft_telegram.bot.internal.{ActionRunner, InlineQueryManager, UpdateManager}
import cc.sukazyo.minecraft_telegram.bot.minecraft.{MinecraftChatMessageListener, MinecraftCommandMessageListener, MinecraftGameMessageListener, MinecraftServerLifecycleListener}
import cc.sukazyo.minecraft_telegram.bot.queries.ListUsers
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.User
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import org.apache.logging.log4j.Logger

class Bot (config: BotConfig)(using logger: Logger) {
	
	val (
		account: TelegramBot,
		bot_user: User
	) = config.loginToBot
	given _this_bot: Bot = this
	given TelegramBot = account
	
	val eventManager: UpdateManager = UpdateManager()
	val queryManager: InlineQueryManager = InlineQueryManager()
	
	private object actionRunner extends ActionRunner
	
	logger `info` s"Logged in to bot successfully : bot @${bot_user.username}[${bot_user.id}]"
	
	ServerMessageEvents.CHAT_MESSAGE `register` MinecraftChatMessageListener()
	ServerMessageEvents.GAME_MESSAGE `register` MinecraftGameMessageListener()
	ServerMessageEvents.COMMAND_MESSAGE `register` MinecraftCommandMessageListener()
	for (i <- MinecraftServerLifecycleListener() :: Nil)
		ServerLifecycleEvents.SERVER_STARTED `register` i.ServerStarted
		ServerLifecycleEvents.SERVER_STOPPED `register` i.ServerStopped
	
	eventManager += BotIgnoringOutdatedMessage()
	eventManager += OnMinecraftCommandExecute()
	eventManager += OnTelegram2Minecraft()
	eventManager += queryManager.QueryEventListener
	
	queryManager += ListUsers()
	
	val startEpochMillis: Long = System.currentTimeMillis()
	this.start()
	
	def start (): Unit = {
		account.setUpdatesListener(eventManager, eventManager.OnGetUpdateFailed)
		actionRunner.start()
		logger `info` "Started Telegram Listener"
	}
	
	def shutdown (): Unit = {
		account.removeGetUpdatesListener()
		actionRunner.setDisabled()
		logger `info` "Stopped Telegram bot"
	}
	
	def runs (func: ActionRunner.Action): Unit = {
		actionRunner.runs(func)
	}
	
	object dsl {
		given Bot = _this_bot
		given TelegramBot = account
	}
	
}
