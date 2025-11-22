package uid.infinity.shampoo.features.commands.impl;

import uid.infinity.shampoo.features.commands.Command;
import net.minecraft.util.Util;

public class FolderCommand extends Command {
    public FolderCommand() {
        super("folder");
    }
    @Override
    public void execute(String[] var1) {
        Util.getOperatingSystem().open("Impossible");
    }
}
