package uid.infinity.shampoo.features.modules.misc;

import com.mojang.authlib.GameProfile;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

public class FakePlayer extends Module {
    private Setting<String> name = str("Name", "shampooclient");
    private OtherClientPlayerEntity player;

    public FakePlayer() {
        super("FakePlayer", "Spawn fake player, dont real", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) return;
            player = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), name.getValue()));
            player.copyFrom(mc.player);
            mc.world.addEntity(player);
            mc.player.setHealth(20);
        }

    @Override
    public void onDisable() {
            mc.world.removeEntity(player.getId(), Entity.RemovalReason.DISCARDED);
        }
}