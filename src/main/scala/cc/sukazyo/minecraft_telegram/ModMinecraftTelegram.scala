package cc.sukazyo.minecraft_telegram

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.{Logger, LogManager}

object ModMinecraftTelegram extends ModInitializer {
	
	val MODID = "minecraft_telegram_connector"
	val NAME = "Minecraft Telegram Connector"
	val VERSION: String = BuildConfig.MOD_VERSION
	val logger: Logger = LogManager.getLogger(NAME)
	
	override def onInitialize (): Unit = {
		logger info "Hello, Minecraft Telegram."
	}
	
}
