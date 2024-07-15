package cc.sukazyo.minecraft_telegram.bot.events

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener}
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}

class BotIgnoringOutdatedMessage (using bot: Bot) extends EventListener with BotExt {
	
	override def onMessage (using event: EventEnv): Unit = {
		
		if event.update.message.date*1000 < bot.startEpochMillis then
			event.setEventCanceled
		
	}
	
}
