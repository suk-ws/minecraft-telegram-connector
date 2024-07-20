package cc.sukazyo.minecraft_telegram.utils

import net.fabricmc.loader.api.FabricLoader
import java.lang.reflect.InvocationTargetException

object ConditionalLoading {
	trait Impl:
		def init(): Unit
}

trait ConditionalLoading {
	
	val conditionMod: String
	val loadClass: String
	
	def init (): AnyRef = {
		if (FabricLoader.getInstance.isModLoaded(this.conditionMod))
			try
				Class.forName(loadClass).getMethod("init").invoke(null)
			catch case e: (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException) =>
				throw new RuntimeException(e)
		null
	}
	
}
