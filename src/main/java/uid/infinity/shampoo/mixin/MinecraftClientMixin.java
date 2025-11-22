package uid.infinity.shampoo.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.exploit.MultiTask;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;


    @ModifyExpressionValue(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"))
    private boolean doItemUseModifyIsBreakingBlock(boolean original) {
        MultiTask multiTask = shampoo.moduleManager.getModuleByClass(MultiTask.class);
        if(multiTask.isEnabled()) return false;
        return original;
    }

    @ModifyExpressionValue(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean handleBlockBreakingModifyIsUsingItem(boolean original) {
        MultiTask multiTask = shampoo.moduleManager.getModuleByClass(MultiTask.class);
        if(multiTask.isEnabled()) return false;
        return original;
    }
}
