package cc.sukazyo.minecraft_telegram.bot.minecraft

import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute
import cc.sukazyo.minecraft_telegram.utils.MinecraftServerExtension.MinecraftServerExt
import org.apache.logging.log4j.Logger

class MinecraftServerLifecycleListener (using bot: Bot)(using logger: Logger) extends BotExt {
	import bot.dsl.given
	
	object ServerStarted extends ServerLifecycleEvents.ServerStarted {
		override def onServerStarted(server: MinecraftServer): Unit = {
			SendMessage(
				minecraftLinkedChat,
				s"""<b>▌🗄Server Status</b>
				   |
				   |🟢 ${server.getServerName} Server Started!""".stripMargin
			).parseMode(ParseMode.HTML)
				.unsafeExecute
			logger debug "synced server started state to telegram"
		}
	}
	
	object ServerStopped extends ServerLifecycleEvents.ServerStopped {
		override def onServerStopped(server: MinecraftServer): Unit = {
			SendMessage(
				minecraftLinkedChat,
				s"""<b>▌🗄Server Status</b>
				   |
				   |🔴 ${server.getServerName} Server Stopped!""".stripMargin
			).parseMode(ParseMode.HTML)
				.unsafeExecute
			logger debug "synced server stopped state to telegram"
		}
	}
	
}
