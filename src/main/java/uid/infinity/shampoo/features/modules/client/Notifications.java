package uid.infinity.shampoo.features.modules.client;

import uid.infinity.shampoo.shampoo;
import uid.infinity.shampoo.features.commands.Command;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class Notifications extends Module {
    private final ArrayList<PlayerEntity> rangeplayers = new ArrayList<>();
    public Setting<Boolean> visualrange = bool("VisualRange", true);

    public Notifications() {
        super("Notification", "Client notifications enable, disable modules and visual range", Category.CLIENT, true, false, false);
    }

    @Override
    public void onTick() {
        if (fullNullCheck()) return;
        if (visualrange.getValue()) {
            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof PlayerEntity player) || entity == mc.player) continue;
                if (!rangeplayers.contains(player)) {
                    rangeplayers.add(player);
                    String message = Formatting.WHITE + player.getName().getString() + Formatting.RED + " has entered your visual range!";
                    Command.sendMessage1(ChatUtil.getRainbow() + shampoo.commandManager.getClientMessage() + " " + message);
                }
            }
            if (!rangeplayers.isEmpty()) {
                for (PlayerEntity player : new ArrayList<>(rangeplayers)) {
                    if (!mc.world.getPlayers().contains(player)) {
                        rangeplayers.remove(player);
                        String message = Formatting.WHITE + player.getName().getString() + Formatting.RED + " has left your visual range!";
                        Command.sendMessage1(ChatUtil.getRainbow() + shampoo.commandManager.getClientMessage() + " " + message);
                    }
                }
            }
        }
    }
}