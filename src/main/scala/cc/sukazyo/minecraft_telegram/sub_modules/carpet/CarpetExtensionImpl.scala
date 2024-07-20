package cc.sukazyo.minecraft_telegram.sub_modules.carpet

import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotLifecycleEvents}
import cc.sukazyo.minecraft_telegram.utils.ConditionalLoading

object CarpetExtensionImpl extends ConditionalLoading.Impl {
	
	def init (): Unit = {
		
		ModMinecraftTelegram.logger.info("Carpet is loaded, enabling telegram connector extensions for carpet.")
		BotLifecycleEvents.BOT_INITIALIZING.register(BotExtension)
		BotLifecycleEvents.BOT_SHUTTING_DOWN.register(BotExtension)
		
	}
	
	private object BotExtension extends BotLifecycleEvents.BotInitializing with BotLifecycleEvents.BotShuttingDown {
		
		override def onBotInitializing (bot: Bot): Unit = {
			bot.queryManager += QueryFreezeStatus()
		}
		
		override def onBotShuttingDown (bot: Bot): Unit = {}
		
	}
	
}
