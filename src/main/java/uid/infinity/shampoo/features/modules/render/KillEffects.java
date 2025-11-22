package uid.infinity.shampoo.features.modules.render;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;

public class KillEffects extends Module {
    public KillEffects() {
        super("KillEffects", "Epic KillEffects for pvp", Category.RENDER, true,false, false);
    }

    @Subscribe public void onDeath(PlayerEntity entity) {
        LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, mc.world);
        lightningEntity.refreshPositionAfterTeleport(entity.getX(), entity.getY(), entity.getZ());
        mc.world.addEntity(lightningEntity);
    }
}