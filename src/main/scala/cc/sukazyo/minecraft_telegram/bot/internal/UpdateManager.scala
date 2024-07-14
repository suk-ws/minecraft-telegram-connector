package cc.sukazyo.minecraft_telegram.bot.internal

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener, EventRuntimeException}
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram.logger
import cc.sukazyo.minecraft_telegram.utils.Log4jExtension.LoggerExt
import cc.sukazyo.std.throwable
import com.google.gson.GsonBuilder
import com.pengrad.telegrambot.{ExceptionHandler, TelegramException, UpdatesListener}
import com.pengrad.telegrambot.model.Update

import java.util
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.ListHasAsScala

class UpdateManager extends UpdatesListener {
	
	private val listeners = ListBuffer[EventListener]()
	
	def register (listeners: EventListener*): Unit =
		this.listeners ++= listeners
	def += (listener: EventListener): Unit =
		this.register(listener)
	def ++= (listener: EventListener*): Unit =
		this.register(listener*)
	
	private class EventRunner (using update: Update) extends Thread {
		this setName s"upd-${update.updateId()}-nn"
		private def updateThreadName (t: String): Unit =
			this setName s"upd-${update.updateId()}-$t"
		
		override def run (): Unit = {
			
			given env: EventEnv = EventEnv(update)
			
			for (i <- listeners)
				if (i.executeFilter)
					runEventListener(i)
			for (i <- listeners)
				runEventPost(i)
			
		}
		
		private def runEventPost (i: EventListener)(using EventEnv): Unit = {
			updateThreadName("#post")
			i.atEventPost
		}
		
		private def runEventListener (i: EventListener)(using EventEnv): Unit = {
			try {
				updateThreadName("message")
				if update.message ne null then i.onMessage
				updateThreadName("edited-message")
				if update.editedMessage ne null then i.onEditedMessage
				updateThreadName("channel-post")
				if update.channelPost ne null then i.onChannelPost
				updateThreadName("edited-channel-post")
				if update.editedChannelPost ne null then i.onEditedChannelPost
				updateThreadName("inline-query")
				if update.inlineQuery ne null then i.onInlineQuery
				updateThreadName("chosen-inline-result")
				if update.chosenInlineResult ne null then i.onChosenInlineResult
				updateThreadName("callback-query")
				if update.callbackQuery ne null then i.onCallbackQuery
				updateThreadName("shipping-query")
				if update.shippingQuery ne null then i.onShippingQuery
				updateThreadName("pre-checkout-query")
				if update.preCheckoutQuery ne null then i.onPreCheckoutQuery
				updateThreadName("poll")
				if update.poll ne null then i.onPoll
				updateThreadName("poll-answer")
				if update.pollAnswer ne null then i.onPollAnswer
				updateThreadName("my-chat-member")
				if update.myChatMember ne null then i.onMyChatMemberUpdated
				updateThreadName("chat-member")
				if update.chatMember ne null then i.onChatMemberUpdated
				updateThreadName("chat-join-request")
				if update.chatJoinRequest ne null then i.onChatJoinRequest
			} catch case e => {
				val errorMessage = StringBuilder()
				errorMessage ++= "Event throws unexpected exception:\n"
				errorMessage ++= throwable.ThrowableExtensions(e).toLogString
				e match
					case actionFailed: EventRuntimeException.ActionFailed =>
						errorMessage ++= "\ntg-api action: response track: "
						errorMessage ++= (GsonBuilder().setPrettyPrinting().create().toJson(
							actionFailed.response
						) indent 4) ++= "\n"
					case _ =>
				logger error errorMessage.toString
			}
		}
		
	}
	
	override def process (updates: util.List[Update]): Int = {
		for (update <- updates.asScala)
			EventRunner(using update).start()
		UpdatesListener.CONFIRMED_UPDATES_ALL
	}
	
	object OnGetUpdateFailed extends ExceptionHandler {
		
		override def onException (e: TelegramException): Unit = {
			
			// This function intended to catch exceptions on update
			//   fetching controlled by Telegram API Client. So that
			//   it won't be directly printed to STDOUT without Morny's
			//   logger. And it can be reported when needed.
			// TelegramException can either contains a caused that infers
			//   a lower level client exception (network err or others);
			//   nor contains a response that means API request failed.
			
			if (e.response != null) {
				import com.google.gson.GsonBuilder
				logger error
					s"""Failed get updates: ${e.getMessage}
					   |  server responses:
					   |${GsonBuilder().setPrettyPrinting().create.toJson(e.response) indent 4}
					   |""".stripMargin
			}
			
			logger error "Failed get updates from Telegram Server:"
			logger errorExceptionSimple e
			
		}
		
	}
	
}
