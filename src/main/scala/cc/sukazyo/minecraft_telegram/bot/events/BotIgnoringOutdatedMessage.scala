package cc.sukazyo.minecraft_telegram.bot.events

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener}
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import org.apache.logging.log4j.Logger

class BotIgnoringOutdatedMessage (using bot: Bot)(using logger: Logger) extends EventListener with BotExt {
	
	override def onMessage (using event: EventEnv): Unit = {
		
		if event.update.message.date*1000L < bot.startEpochMillis then
			logger `debug` "telegram message is cancelled"
			event.setEventCanceled
		
	}
	
}
