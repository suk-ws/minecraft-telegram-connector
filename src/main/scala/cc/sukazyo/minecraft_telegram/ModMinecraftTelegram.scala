package cc.sukazyo.minecraft_telegram

import cc.sukazyo.minecraft_telegram.bot.{Bot, BotConfig}
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.{Logger, LogManager}

object ModMinecraftTelegram extends ModInitializer {
	
	val MODID = "minecraft_telegram_connector"
	val NAME = "Minecraft Telegram Connector"
	val VERSION: String = BuildConfig.MOD_VERSION
	
	val logger: Logger = LogManager.getLogger(NAME)
	var SERVER: MinecraftServer = _
	
	var bot: Bot = _
	
	object ServerStartingCallback extends ServerLifecycleEvents.ServerStarting {
		override def onServerStarting(server: net.minecraft.server.MinecraftServer): Unit = {
			
			ModMinecraftTelegram.SERVER = server
			if !server.isDedicated then
				logger warn "Server is NOT a dedicated server, will NOT RUN the telegram connector bot!"
				logger warn s"This mod ($NAME) is ONLY AVAILABLE on a DEDICATED SERVER!!!"
				return;
			
			try {
				bot = Bot(BotConfig(
					// TODO: Bot Config
					api_server = None,
					username = None,
					token = ""
				))(using logger)
			} catch case e: BotConfig.LoginFailedException =>
				logger error "Failed to login to the Telegram Bot !"
				logger error e
				logger warn s"$NAME will NOT WORKS!!!"
			
		}
	}
	
	override def onInitialize (): Unit = {
		
		ServerLifecycleEvents.SERVER_STARTING register this.ServerStartingCallback
		
	}
	
}
