package cc.sukazyo.minecraft_telegram.bot.internal

import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.bot.internal.ActionRunner.Action
import cc.sukazyo.minecraft_telegram.utils.Log4jExtension.LoggerExt
import org.apache.logging.log4j.Logger

import scala.collection.mutable

object ActionRunner {
	type Action = () => Unit
}

class ActionRunner (val actionPool: mutable.Queue[Action] = mutable.Queue.empty)(using logger: Logger)
extends Thread(s"${ModMinecraftTelegram.NAME}/ActionRunner") {
	
	private var disabled = false
	def setDisabled(): Unit = disabled = true
	
	def call(): Unit = this.interrupt()
	
	def runs (func: Action): Unit =
		this.actionPool.enqueue(func)
		this.call()
	
	override def run (): Unit = {
		while (true) {
			try {
				val elem = this.actionPool.dequeue()
				try elem.apply()
				catch case e: Throwable =>
					logger error "Failed to execute an bot action:"
					logger errorExceptionSimple e
			} catch case e: NoSuchElementException => ()
			if this.actionPool.isEmpty then
				if disabled then
					logger info s"ActionRunner is disabled with ${this.actionPool.size} remaining tasks"
					return;
				try Thread.sleep(Long.MaxValue)
				catch case e: InterruptedException => ()
		}
	}
	
}
