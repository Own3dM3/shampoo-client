package uid.infinity.shampoo.features.modules.render;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {

    public Setting<Mode> mode = register(new Setting<>("Mode", Mode.GAMMA));

    private float originalGamma = -1f;

    public FullBright() {
        super("FullBright", "Makes everything bright", Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        if (mode.getValue() == Mode.GAMMA) {
            originalGamma = mc.options.getGamma().getValue().floatValue();
            mc.options.getGamma().setValue(1000.0);
        } else if (mode.getValue() == Mode.POTION && mc.player != null) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onDisable() {
        if (mode.getValue() == Mode.GAMMA) {
            if (originalGamma != -1f) {
                mc.options.getGamma().setValue((double) originalGamma);
            }
        } else if (mode.getValue() == Mode.POTION && mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    public enum Mode {
        GAMMA,
        POTION
    }
}