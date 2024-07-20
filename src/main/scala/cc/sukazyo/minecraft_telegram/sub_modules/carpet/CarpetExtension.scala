package cc.sukazyo.minecraft_telegram.sub_modules.carpet

import cc.sukazyo.minecraft_telegram.utils.ConditionalLoading

class CarpetExtension extends ConditionalLoading {
	override val conditionMod = "carpet"
	override val loadClass = "cc.sukazyo.minecraft_telegram.sub_modules.carpet.CarpetExtensionImpl"
}
