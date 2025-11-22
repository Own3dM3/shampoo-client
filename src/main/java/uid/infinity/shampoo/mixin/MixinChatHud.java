package uid.infinity.shampoo.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.util.ColorUtil;
import uid.infinity.shampoo.util.CustomFormatting;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.awt.*;
  // пофикси этот миксин пожалуйста
@Mixin(ChatHud.class)
public abstract class MixinChatHud {

    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;content()Lnet/minecraft/text/OrderedText;")
    )
    private OrderedText modifyChatTextExpression(OrderedText original) {

        if (original == null) return null;
        return processOrderedText(original);
    }

    private OrderedText processOrderedText(OrderedText orderedText) {
        if (orderedText == null) return null;

        MutableText builder = Text.empty();
        int[] index = {0};

        orderedText.accept((i, style, codePoint) -> {
            String charStr = String.valueOf(Character.toChars(codePoint));

            if (style.getColor() != null) {
                String colorName = style.getColor().toString().toLowerCase();

                if (colorName.equals(CustomFormatting.CLIENT.getName())) {
                    builder.append(Text.literal(charStr).setStyle(Style.EMPTY.withColor(
                            TextColor.fromRgb(shampoo.colorManager.getColor().getRGB())
                    )));
                    index[0]++;
                    return true;
                }

                if (colorName.equals(CustomFormatting.RAINBOW.getName())) {
                    long colorIndex = (long) index[0] * 50L;
                    Color rainbowColor = ColorUtil.getOffsetRainbow(colorIndex);
                    builder.append(Text.literal(charStr).setStyle(Style.EMPTY.withColor(
                            TextColor.fromRgb(rainbowColor.getRGB())
                    )));
                    index[0]++;
                    return true;
                }
            }

            builder.append(Text.literal(charStr).setStyle(style));
            index[0]++;
            return true;
        });

        return builder.asOrderedText();
    }
}