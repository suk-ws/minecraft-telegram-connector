package cc.sukazyo.minecraft_telegram.bot

import cc.sukazyo.cono.morny.system.telegram_api.Standardize.ChatID
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram

trait BotExt (using bot: Bot) {
	
	def minecraftLinkedChat: ChatID = ModMinecraftTelegram.config.telegram.chat_id
	
}
