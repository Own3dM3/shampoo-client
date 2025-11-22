package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class KillSound extends Module {
    public Setting<Mod> mode = this.register(new Setting<>("Mode", Mod.Thunder));

    public enum Mod {
        Thunder, Xp, Mace
    }

    public KillSound() {
        super("KillSound", "Epic KillSounds for pvp", Category.MISC, true, false, false);
    }

    @Override
    public void onDeath(PlayerEntity player) {
        if (mode.getValue() == Mod.Thunder) {
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 3F, 2F);
        } else if (mode.getValue() == Mod.Xp) {
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 3F, 2F);
        } else if (mode.getValue() == Mod.Mace) {
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_MACE_SMASH_GROUND, SoundCategory.PLAYERS, 3F, 2F);
        }
    }
}