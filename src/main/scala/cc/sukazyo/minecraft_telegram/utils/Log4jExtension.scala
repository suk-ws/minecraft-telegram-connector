package cc.sukazyo.minecraft_telegram.utils

import org.apache.logging.log4j.Logger

object Log4jExtension {
	
	implicit class LoggerExt (logger: Logger) {
		
		def errorExceptionSimple (e: Throwable): Unit = {
			logger error e.toString
			var caused: Throwable = e.getCause
			while (caused != null)
				logger error s" - Caused by: ${caused.toString}"
				caused = caused.getCause
		}
		
	}
	
}
