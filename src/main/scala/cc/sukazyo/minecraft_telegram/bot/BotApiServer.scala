package cc.sukazyo.minecraft_telegram.bot

case class BotApiServer (
	api_url: String,
	file_api_url: String,
	use_test_server: Boolean = false
)

object BotApiServer {
	
	object Defaults extends BotApiServer(
		api_url = "https://api.telegram.org/bot",
		file_api_url = "https://api.telegram.org/file/bot"
	)
	
}

