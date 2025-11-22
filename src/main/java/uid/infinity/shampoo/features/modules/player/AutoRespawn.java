package uid.infinity.shampoo.features.modules.player;

import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", "Auto respawn you, when you die", Category.PLAYER, true,false,false);
    }
    @Override
    public void onTick() {
        if (nullCheck())
            return;
        if (mc.currentScreen instanceof DeathScreen)
        {
            mc.player.requestRespawn();

        }
    }
}