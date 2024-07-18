package cc.sukazyo.minecraft_telegram.bot

import cc.sukazyo.minecraft_telegram.bot.BotExt.Predef
import cc.sukazyo.minecraft_telegram.connector

trait BotExt (using bot: Bot) extends Predef with connector.Predef

object BotExt {
	
	object Predef extends Predef
	trait Predef {
		
		object Prefixes {
			
			val SERVER_IDENTITY = "▌"
			val SERVER_MESSAGE = "▌ "
			
		}
	}
	
}
