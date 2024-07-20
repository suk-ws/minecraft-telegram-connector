package cc.sukazyo.minecraft_telegram.sub_modules.carpet

import carpet.fakes.MinecraftServerInterface
import cc.sukazyo.cono.morny.system.telegram_api.inline_query.{InlineQueryUnit, ITelegramQuery}
import cc.sukazyo.minecraft_telegram.connector.Predef
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle

class QueryFreezeStatus extends ITelegramQuery with Predef {
	
	override def query (event: Update): List[InlineQueryUnit[?]] | Null = {
		import event.inlineQuery
		
		// todo: there may be a config to enable/disable
		if inlineQuery.query.nonEmpty then return null
		
		val trm = minecraftServer.asInstanceOf[MinecraftServerInterface].getTickRateManager
		val runningStatus =
			if trm.gameIsPaused then
				if trm.deeplyFrozen then
					"DEEP FROZEN"
				else "FROZEN"
			else "running"
		
		List(
			InlineQueryUnit(
				InlineQueryResultArticle(
					s"[mc-tg/carpet/freezing/${System.currentTimeMillis}]",
					s"The world is $runningStatus.",
					s"The world is $runningStatus.",
				)
			).cacheTime(1).isPersonal(false)
		)
		
	}
}
