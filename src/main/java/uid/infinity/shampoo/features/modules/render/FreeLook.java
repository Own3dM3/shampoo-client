package uid.infinity.shampoo.features.modules.render;

import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.client.option.Perspective;

public final class FreeLook extends Module {
    private Perspective previousPerspective;

    public FreeLook() {
        super("FreeLook", "Toggles third-person view (F5)", Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {

        previousPerspective = mc.options.getPerspective();
        mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onDisable() {
        if (previousPerspective != null) {
            mc.options.setPerspective(previousPerspective);
        } else {
            mc.options.setPerspective(Perspective.FIRST_PERSON);
        }
    }
}