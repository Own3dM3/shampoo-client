package uid.infinity.shampoo.features.modules.movement;

import uid.infinity.shampoo.features.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Box;
import uid.infinity.shampoo.util.traits.*;

public class Parkour extends Module {
    public Parkour(){
        super("Parkour","Automatically jumps at the edge of blocks",Category.MOVEMENT,true,false, false);
    }
    private boolean jumping = false;

    @Override
    public void onUpdate() {
        if (Util.mc.player == null || Util.mc.world == null) return;

        if (Util.mc.player.isOnGround() && !Util.mc.player.isSneaking() && Util.mc.world.isSpaceEmpty(Util.mc.player.getBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001))) {
            Util.mc.options.jumpKey.setPressed(true);
            jumping = true;
        } else if (jumping) {
            jumping = false;
            Util.mc.options.jumpKey.setPressed(false);
        }
    }

    @Override
    public void onDisable() {
        if (jumping) {
            Util.mc.options.jumpKey.setPressed(false);
            jumping = false;
        }
    }
}