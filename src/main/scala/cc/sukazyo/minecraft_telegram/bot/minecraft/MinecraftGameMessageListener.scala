package cc.sukazyo.minecraft_telegram.bot.minecraft

import cc.sukazyo.minecraft_telegram.bot.{Bot, BotExt}
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text

class MinecraftGameMessageListener (using bot: Bot) extends ServerMessageEvents.GameMessage with BotExt {
	
	override def onGameMessage (server: MinecraftServer, message: Text, overlay: Boolean): Unit = {
		???
	}
	
}
