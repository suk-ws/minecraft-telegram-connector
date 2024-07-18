package cc.sukazyo.minecraft_telegram.mixins;

import cc.sukazyo.minecraft_telegram.connector.ConnectorConsole;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommandSource.class)
public abstract class MixinSendToOps implements CommandSource {
	
	@Final @Shadow private MinecraftServer server;
	
	@Inject(method = "sendToOps", at = @At("RETURN"))
	public void sendToOps_append (Text message, CallbackInfo ci) {
		
		if (!ConnectorConsole.isAdminConsole(this) && this.server.getGameRules().getBoolean(GameRules.LOG_ADMIN_COMMANDS)) {
			ConnectorConsole.broadcastToTelegramConsole(message, (ServerCommandSource)(Object)this);
		}
		
	}
	
}
