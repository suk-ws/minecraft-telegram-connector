package cc.sukazyo.minecraft_telegram.connector

import cc.sukazyo.cono.morny.system.telegram_api.Standardize.ChatID
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.bot.Bot

trait Predef {
	
	def minecraftLinkedChat: ChatID = ModMinecraftTelegram.config.telegram.chat_id
	
}

object Predef extends Predef {
	
	object Ext extends Ext
	trait Ext {
		def connectorBot: Bot = ModMinecraftTelegram.bot
	}
	
}
