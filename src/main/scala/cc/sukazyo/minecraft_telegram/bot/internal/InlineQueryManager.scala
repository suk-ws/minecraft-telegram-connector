package cc.sukazyo.minecraft_telegram.bot.internal

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener}
import cc.sukazyo.cono.morny.system.telegram_api.inline_query.{InlineQueryUnit, ITelegramQuery}
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.InlineQueryResult
import com.pengrad.telegrambot.request.AnswerInlineQuery

import cc.sukazyo.cono.morny.system.telegram_api.TelegramExtensions.Requests.unsafeExecute

import scala.collection.mutable.ListBuffer

class InlineQueryManager (using bot: Bot) extends BotExt {
	import bot.dsl.given
	
	private val queriesInstances: ListBuffer[ITelegramQuery] = ListBuffer.empty
	
	def register (queries: ITelegramQuery*): Unit =
		this.queriesInstances ++= queries
	def += (queries: ITelegramQuery): Unit =
		this.register(queries)
	def ++= (queries: ITelegramQuery*): Unit =
		this.register(queries*)
	
	private def doQueries (using update: Update): List[InlineQueryUnit[?]] =
		queriesInstances.toList
			.flatMap(_.query(update))
			.filterNot(_ == null)
	
	object QueryEventListener extends EventListener {
		
		override def onInlineQuery (using event: EventEnv): Unit = {
			
			val answers = InlineQueryManager.this.doQueries(using event.update)
			
			if answers.isEmpty then return;
			
			val cacheTime = answers.map(_.cacheTime).min
			val isPersonal = answers.exists(_.isPersonal)
			val resultAnswers = answers.map(_.result.asInstanceOf[InlineQueryResult[?]])
			
			AnswerInlineQuery(
				event.update.inlineQuery.id,
				resultAnswers*
			).cacheTime(cacheTime).isPersonal(isPersonal)
				.unsafeExecute
			
			event.setEventOk
			
		}
		
	}
	
}
