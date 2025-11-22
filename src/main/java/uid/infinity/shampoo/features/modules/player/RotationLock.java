package uid.infinity.shampoo.features.modules.player;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class RotationLock extends Module {
    public Setting<Integer> yaw = num("Yaw", 90, 1, 180);
    public Setting<Integer> pitch = num("Pitch", 90, 1, 180);
    public RotationLock() {
        super("RotationLock", "Lock your rotation", Category.PLAYER, true,false,false);
    }
    @Override public void onUpdate() {
        if (nullCheck()) return;
      mc.player.setYaw(yaw.getValue());
      mc.player.setPitch(pitch.getValue());

    }
}