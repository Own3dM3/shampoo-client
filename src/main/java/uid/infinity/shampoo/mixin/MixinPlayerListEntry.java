package uid.infinity.shampoo.mixin;

import com.mojang.authlib.GameProfile;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.modules.client.Capes;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static uid.infinity.shampoo.util.traits.Util.mc;

@Mixin(PlayerListEntry.class)
public class MixinPlayerListEntry {
    @Shadow
    @Final
    private GameProfile profile;

    @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
    private void getSkinTextures(CallbackInfoReturnable<SkinTextures> info) {
        if (((profile.getName().equals(mc.player.getGameProfile().getName()) && profile.getId().equals(mc.player.getGameProfile().getId()))) && shampoo.moduleManager.getModuleByClass(Capes.class).isEnabled() && shampoo.moduleManager.getModuleByClass(Capes.class).getCapeTexture() != null) {
            Identifier identifier = shampoo.moduleManager.getModuleByClass(Capes.class).getCapeTexture();
            SkinTextures texture = info.getReturnValue();

            info.setReturnValue(new SkinTextures(texture.texture(), texture.textureUrl(), identifier, identifier, texture.model(), texture.secure()));
        }
    }
}