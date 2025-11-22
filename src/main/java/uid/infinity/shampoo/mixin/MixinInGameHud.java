package uid.infinity.shampoo.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.impl.Render2DEvent;
import uid.infinity.shampoo.features.modules.render.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static uid.infinity.shampoo.util.traits.Util.EVENT_BUS;
// пофикси этот миксин пожалуйста
@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(method = "render", at = @At("RETURN"))
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (MinecraftClient.getInstance().inGameHud.getDebugHud().shouldShowDebugHud()) return;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);


        Render2DEvent event = new Render2DEvent(context, tickCounter.getTickProgress(true));
        EVENT_BUS.post(event);

        GL11.glDisable(GL11.GL_LINE_SMOOTH);

    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderStatusEffectOverlay(CallbackInfo info) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled() && noRender.potions.getValue()) info.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo callbackInfo) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled() && noRender.portaloverlay.getValue())
            callbackInfo.cancel();

    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo callbackInfo) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled() && noRender.scoarboard.getValue())
            callbackInfo.cancel();

    }
    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderScoreboardSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo callbackInfo) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled()&& noRender.scoarboard.getValue())
            callbackInfo.cancel();
    }
    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    public void renderHeldItemTooltipHook(DrawContext context, CallbackInfo callbackInfo) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled() && noRender.hotbaritemname.getValue())
            callbackInfo.cancel();
    }
    @ModifyArgs(method = "renderMiscOverlays", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0))
    private void renderMiscOverlays(Args args) {
        NoRender noRender = shampoo.moduleManager.getModuleByClass(NoRender.class);
        if (noRender.isEnabled() && noRender.pumkinOverlay.getValue())
            args.set(2, 0f);


    }






}


