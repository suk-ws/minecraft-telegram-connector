package cc.sukazyo.minecraft_telegram

import cc.sukazyo.minecraft_telegram.bot.{Bot, BotConfig}
import cc.sukazyo.minecraft_telegram.config.{Config, ConfigManager}
import cc.sukazyo.minecraft_telegram.utils.Log4jExtension.*
import cc.sukazyo.restools.ResourcePackage
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.{Logger, LogManager}

object ModMinecraftTelegram extends ModInitializer {
	
	val MODID = "minecraft_telegram_connector"
	val NAME = "Minecraft Telegram Connector"
	val VERSION: String = BuildConfig.MOD_VERSION
	
	given logger: Logger = LogManager.getLogger(NAME)
	val resources: ResourcePackage = ResourcePackage.get("minecraft_telegram_connector.mixins.json")
	var SERVER: MinecraftServer = _
	
	var bot: Bot = _
	var config: Config = _
	
	private object ServerStartingCallback extends ServerLifecycleEvents.ServerStarting {
		override def onServerStarting(server: net.minecraft.server.MinecraftServer): Unit = {
			
			ModMinecraftTelegram.SERVER = server
			if !server.isDedicated then
				logger warn "Server is NOT a dedicated server, will NOT RUN the telegram connector bot!"
				logger warn s"This mod ($NAME) is ONLY AVAILABLE on a DEDICATED SERVER!!!"
				return;
			
			try {
				bot = Bot(config.bot)
			} catch case e: BotConfig.LoginFailedException =>
				logger error "Failed to initialize to the Telegram Bot !"
				logger errorExceptionSimple e
				logger warn s"$NAME will NOT WORKS!!!"
			
		}
	}
	
	private object ServerStoppingCallback extends ServerLifecycleEvents.ServerStopping {
		override def onServerStopping(server: net.minecraft.server.MinecraftServer): Unit = {
			if bot != null then
				bot.shutdown()
		}
	}
	
	override def onInitialize (): Unit = {
		
		this.config = ConfigManager.read[Config]("config")
		
		ServerLifecycleEvents.SERVER_STARTING register this.ServerStartingCallback
		ServerLifecycleEvents.SERVER_STOPPING register this.ServerStoppingCallback
		
	}
	
}
