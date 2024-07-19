package cc.sukazyo.minecraft_telegram.bot.queries

import cc.sukazyo.cono.morny.system.telegram_api.inline_query.{InlineQueryUnit, ITelegramQuery}
import cc.sukazyo.minecraft_telegram.connector.Predef
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle

class ListUsers extends ITelegramQuery with Predef {
	
	override def query (event: Update): List[InlineQueryUnit[?]] | Null = {
		import event.inlineQuery
		
		// todo: there may be a config to enable/disable
		if inlineQuery.query.nonEmpty then return null
		
		List(
			InlineQueryUnit(
				InlineQueryResultArticle(
					s"[mc-tg/list/${System.currentTimeMillis}]",
					s"Current online users (${minecraftServer.getCurrentPlayerCount}/${minecraftServer.getMaxPlayerCount}):",
					// todo: formatting
					s"""Current online users (${minecraftServer.getCurrentPlayerCount}/${minecraftServer.getMaxPlayerCount}):
					   |
					   |${minecraftServer.getPlayerNames.mkString("\n")}""".stripMargin
				)
			).cacheTime(1).isPersonal(false)
		)
		
	}
}
