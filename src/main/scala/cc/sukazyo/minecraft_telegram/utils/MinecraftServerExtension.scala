package cc.sukazyo.minecraft_telegram.utils

import net.minecraft.server.MinecraftServer

object MinecraftServerExtension {
	
	implicit class MinecraftServerExt (server: MinecraftServer) {
		
		def getServerName: String = server.getServerMotd
		
	}
	
}
