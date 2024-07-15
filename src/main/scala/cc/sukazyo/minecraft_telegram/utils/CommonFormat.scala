package cc.sukazyo.minecraft_telegram.utils

object CommonFormat {
	
	def formatDuration (durationSeconds: Int): String =
		val sb = new StringBuilder()
		if (durationSeconds > 60 * 60 * 24) sb ++= (durationSeconds / (60 * 60 * 24)).toString ++= ":"
		if (durationSeconds > 60 * 60) sb ++= (durationSeconds / (60 * 60) % 24).toString ++= ":"
		if (durationSeconds > 60) sb ++= (durationSeconds / 60 % 60).toString ++= ":"
		sb ++= (durationSeconds % 60).toString
		sb.toString
	
}
