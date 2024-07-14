package cc.sukazyo.minecraft_telegram.bot.events

import cc.sukazyo.cono.morny.system.telegram_api.event.{EventEnv, EventListener}
import cc.sukazyo.cono.morny.system.telegram_api.formatting.TelegramFormatter.*
import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import cc.sukazyo.minecraft_telegram.utils.Magics
import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.minecraft_telegram.utils.MinecraftServerExtension.MinecraftServerExt
import net.minecraft.text.{Style, Text}
import org.apache.logging.log4j.Logger

class OnTelegram2Minecraft (using bot: Bot)(using logger: Logger) extends EventListener with BotExt {
	
	override def onMessage (using event: EventEnv): Unit = {
		import event.update.message
		
		if message.chat.id != minecraftLinkedChat then return;
		
		val plainMessage = message.text
		val senderName = message.from.fullname
		val senderNameTag = s"<<$senderName>>"
		
		val mcMessage = Text.empty()
			.append(Text.literal(senderNameTag).setStyle(Style.EMPTY.withColor(Magics.COLOR_TELEGRAM_ICON)))
			.append(Text.literal(" "))
			.append(Text.literal(plainMessage))
		
		ModMinecraftTelegram.SERVER.broadcastMessage(mcMessage)
		
		event.setEventOk
		
	}
	
}
