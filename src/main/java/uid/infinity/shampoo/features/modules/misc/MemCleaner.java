package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.text.Text;

public class MemCleaner extends Module {

    public MemCleaner() {
        super("MemCleaner", "Clean your RAM", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        cleanMemory();
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal("§a[shampooclient] Memory has been successfully cleared!"), false);
        }
        disable();
    }

    private void cleanMemory() {
        System.gc();
    }
}