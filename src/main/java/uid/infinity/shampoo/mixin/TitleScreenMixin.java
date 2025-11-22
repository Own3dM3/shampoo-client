package uid.infinity.shampoo.mixin;

import uid.infinity.shampoo.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static uid.infinity.shampoo.util.traits.Util.mc;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    public TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        MutableText namePart = Text.literal(shampoo.NAME)
                .formatted(Formatting.BOLD)
                .withColor(0x6A5ACD);

        MutableText versionPart = Text.literal(" v" + BuildConfig.VERSION)
                .formatted(Formatting.GRAY);

        String buildDate = BuildConfig.BUILD_TIME;
        if (buildDate.length() >= 10) {
            buildDate = buildDate.substring(0, 10);
        }
        MutableText datePart = Text.literal(" • " + buildDate)
                .formatted(Formatting.DARK_GRAY);

        Text fullText = namePart.append(versionPart).append(datePart);

        context.drawTextWithShadow(mc.textRenderer, fullText, 2, 2, 0xFFFFFFFF);
    }
}