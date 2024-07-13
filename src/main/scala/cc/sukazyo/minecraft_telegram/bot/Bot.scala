package cc.sukazyo.minecraft_telegram.bot

import cc.sukazyo.minecraft_telegram.bot.events.OnMinecraftCommandExecute
import cc.sukazyo.minecraft_telegram.bot.internal.UpdateManager
import cc.sukazyo.minecraft_telegram.bot.minecraft.{MinecraftChatMessageListener, MinecraftGameMessageListener, MinecraftServerLifecycleListener}
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
	
	logger info s"Logged in to bot successfully : bot @${bot_user.username}[${bot_user.id}]"
	
	ServerMessageEvents.CHAT_MESSAGE register MinecraftChatMessageListener()
	ServerMessageEvents.GAME_MESSAGE register MinecraftGameMessageListener()
	for (i <- MinecraftServerLifecycleListener() :: Nil)
		ServerLifecycleEvents.SERVER_STARTED register i.ServerStarted
		ServerLifecycleEvents.SERVER_STOPPED register i.ServerStopped
	
	eventManager += OnMinecraftCommandExecute()
	
	account.setUpdatesListener(eventManager, eventManager.OnGetUpdateFailed)
	
	def shutdown (): Unit = {
		account.removeGetUpdatesListener()
		account.shutdown()
	}
	
	object dsl {
		given Bot = _this_bot
		given TelegramBot = account
	}
	
}
