package cc.sukazyo.minecraft_telegram.connector

import cc.sukazyo.cono.morny.system.telegram_api.Standardize.ChatID
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.bot.Bot
import net.minecraft.server.MinecraftServer

trait Predef {
	
	def minecraftLinkedChat: ChatID = ModMinecraftTelegram.config.telegram.chat_id
	
	def minecraftServer: MinecraftServer = ModMinecraftTelegram.SERVER
	
}

object Predef extends Predef {
	
	object Ext extends Ext
	trait Ext {
		def connectorBot: Bot = ModMinecraftTelegram.bot
	}
	
}
