package uid.infinity.shampoo.features.modules.misc;

import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.client.option.NarratorMode;

public class NoNarrator extends Module {
    public NoNarrator(){
        super("NoNarrator","Disable narrator",Category.MISC,true,false,false);
    }

    @Override
    public void onTick(){
        if (fullNullCheck()) return;

        mc.options.getNarrator().setValue(NarratorMode.OFF);
    }
}
