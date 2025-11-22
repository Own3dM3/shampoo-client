package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.misc.BetterChat;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    private static boolean isProcessing = false;

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void onSendMessage(String message, boolean addToHistory, CallbackInfo ci) {
        if (isProcessing) return;

        var betterChat = shampoo.moduleManager.getModuleByClass(BetterChat.class);
        if (betterChat == null || !betterChat.isEnabled() || !betterChat.suffix.getValue()) {
            return;
        }

        if (message.startsWith("/") || message.startsWith(shampoo.commandManager.getPrefix())) {
            return;
        }

        isProcessing = true;
        String newMessage = message + " " + betterChat.message.getValue();
        ((ChatScreen) (Object) this).sendMessage(newMessage, addToHistory);
        isProcessing = false;

        ci.cancel();
    }
}