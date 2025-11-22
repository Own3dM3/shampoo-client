package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.commands.Command;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;
import uid.infinity.shampoo.util.traits.Util;
import net.minecraft.util.Formatting;
 // о господи блять
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AutoWin extends Module {
    public Setting<Integer> minutes = register(new Setting<>("Minutes", 1, 1, 5));

    private Timer shutdownTimer = null;

    public AutoWin() {
        super("AutoWin", "Automatically shuts down your PC after a set time", Category.MISC, false, false, false);
    }

    @Override
    public void onEnable() {
        if (Util.mc.player == null) {
            disable();
            return;
        }

        int delayMinutes = minutes.getValue();
        long delayMillis = delayMinutes * 60_000L;

        shutdownTimer = new Timer("AutoWin-Shutdown-Timer", true);
        shutdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    ProcessBuilder pb;
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        pb = new ProcessBuilder("shutdown", "/s", "/t", "10");
                    } else {
                        pb = new ProcessBuilder("shutdown", "-h", "+1");
                    }
                    pb.start();
                    Command.sendMessage(Formatting.DARK_GREEN + "Shutdown command executed!");
                } catch (IOException e) {
                    Command.sendMessage(Formatting.RED + "Failed to initiate shutdown: " + e.getMessage());
                }
            }
        }, delayMillis);

        Command.sendMessage(
                Formatting.DARK_GREEN + "PC will shut down in " +
                        Formatting.YELLOW + delayMinutes +
                        Formatting.DARK_GREEN + " minute(s)."
        );
    }

    @Override
    public void onDisable() {
        if (shutdownTimer != null) {
            shutdownTimer.cancel();
            shutdownTimer = null;
            Command.sendMessage(Formatting.GOLD + "AutoWin timer cancelled.");
        }
    }
}