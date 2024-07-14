package cc.sukazyo.minecraft_telegram.utils

import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text

import scala.jdk.CollectionConverters.ListHasAsScala

object MinecraftServerExtension {
	
	implicit class MinecraftServerExt (server: MinecraftServer) {
		
		def getServerName: String = server.getServerMotd
		
		def broadcastMessage (message: Text): Unit = {
			
			server.sendMessage(message)
			server.getPlayerManager.getPlayerList.asScala.foreach { player =>
				player.sendMessage(message)
			}
			
		}
		
	}
	
}
