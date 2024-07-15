package cc.sukazyo.minecraft_telegram.config

import cc.sukazyo.minecraft_telegram.ModMinecraftTelegram
import cc.sukazyo.restools.{ResourceDirectory, ResourceFile}
import io.circe.Decoder
import net.fabricmc.loader.api.FabricLoader

import java.io.{FileInputStream, FileOutputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.Path

object ConfigManager {
	
	private lazy val configRoot: Path = FabricLoader.getInstance()
		.getConfigDir.resolve(s"${ModMinecraftTelegram.MODID}")
//	private lazy val defaultsRoot: ResourceDirectory = ModMinecraftTelegram.resources
//		.getDirectory("assets", "minecraft_telegram", "config")
	
	def read [ConT] (config: String)(implicit d: Decoder[ConT]): ConT = {
		
		val filePath = configRoot.resolve(s"$config.yaml")
		val parent_file = filePath.getParent.toFile
		val file = filePath.toFile
		
		// set default config
		if !parent_file.exists then
			parent_file.mkdirs
		if !file.exists then
			// TODO: there's bugs in resource-tools, disabled temporarily
			throw new Exception(s"Config file not found: $filePath")
//			FileOutputStream(file)
//				.write(getDefaultConfig(config).read.readAllBytes)
		
		val fileContent = String(FileInputStream(file).readAllBytes, StandardCharsets.UTF_8)
		import io.circe.yaml.v12.parser
		parser
			.parse(fileContent)
			.flatMap(_.as[ConT])
			.toTry
			.get
		
	}
	
	private def getRealConfig (config: String): Path =
		configRoot.resolve(s"$config.yaml")
	
//	private def getDefaultConfig (config: String): ResourceFile =
//		defaultsRoot.getFile(s"$config.yaml")
	
}
