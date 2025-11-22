package uid.infinity.shampoo.features.commands.impl;

import uid.infinity.shampoo.features.commands.Command;

public class KillCommand extends Command {
    public KillCommand() {
        super("kill");
    }

    @Override
    public void execute(String[] commands) {
        mc.player.networkHandler.sendCommand("kill");
    }
}
