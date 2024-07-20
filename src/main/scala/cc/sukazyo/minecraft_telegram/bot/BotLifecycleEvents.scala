package cc.sukazyo.minecraft_telegram.bot

import net.fabricmc.fabric.api.event.{Event, EventFactory}

object BotLifecycleEvents {
	
	trait BotInitializing:
		def onBotInitializing (bot: Bot): Unit
	val BOT_INITIALIZING: Event[BotInitializing] = EventFactory.createArrayBacked(
		classOf[BotInitializing],
		cbs => bot => cbs.foreach(_.onBotInitializing(bot))
	)
	
	trait BotShuttingDown:
		def onBotShuttingDown (bot: Bot): Unit
	val BOT_SHUTTING_DOWN: Event[BotShuttingDown] = EventFactory.createArrayBacked(
		classOf[BotShuttingDown],
		cbs => bot => cbs.foreach(_.onBotShuttingDown(bot))
	)
	
}
