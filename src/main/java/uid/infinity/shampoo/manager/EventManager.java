package uid.infinity.shampoo.manager;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.event.impl.*;
import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.event.Stage;
import uid.infinity.shampoo.features.Feature;
import uid.infinity.shampoo.features.commands.Command;
import uid.infinity.shampoo.util.CaptureMark;
import uid.infinity.shampoo.util.models.Timer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.Formatting;

public class EventManager extends Feature {
    private final Timer logoutTimer = new Timer();

    public void init() {
        EVENT_BUS.register(this);
    }

    public void onUnload() {
        EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (!fullNullCheck()) {
            shampoo.moduleManager.onUpdate();
            shampoo.moduleManager.sortModules(true);
            onTick();
        }
    }

    public void onTick() {
        if (fullNullCheck()) return;
        shampoo.moduleManager.onTick();
        CaptureMark.tick();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F) continue;
            EVENT_BUS.post(new DeathEvent(player));
            shampoo.moduleManager.onDeath(player);
        }
    }

    @Subscribe
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (fullNullCheck()) return;
        if (event.getStage() == Stage.PRE) {
            shampoo.speedManager.updateValues();
            shampoo.positionManager.updatePosition();
        }
        if (event.getStage() == Stage.POST) {
            shampoo.positionManager.restorePosition();
        }
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        shampoo.serverManager.onPacketReceived();
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket)
            shampoo.serverManager.update();
    }

    @Subscribe
    public void onWorldRender(Render3DEvent event) {
        shampoo.moduleManager.onRender3D(event);
    }

    @Subscribe public void onRenderGameOverlayEvent(Render2DEvent event) {
        shampoo.moduleManager.onRender2D(event);
    }

    @Subscribe public void onKeyInput(KeyEvent event) {
        shampoo.moduleManager.onKeyPressed(event.getKey());
    }

    @Subscribe public void onChatSent(ChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.cancel();
            try {
                if (event.getMessage().length() > 1) {
                    shampoo.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage(Formatting.RED + "An error occurred while running this command. Check the log!");
            }
        }
    }
}