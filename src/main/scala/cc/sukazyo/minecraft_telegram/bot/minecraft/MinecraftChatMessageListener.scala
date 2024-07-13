package cc.sukazyo.minecraft_telegram.bot.minecraft

import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.network.message.{MessageType, SignedMessage}
import net.minecraft.server.network.ServerPlayerEntity

class MinecraftChatMessageListener (using bot: Bot) extends ServerMessageEvents.ChatMessage with BotExt {
	
	override def onChatMessage (message: SignedMessage, sender: ServerPlayerEntity, params: MessageType.Parameters): Unit = {
		???
	}
	
}
