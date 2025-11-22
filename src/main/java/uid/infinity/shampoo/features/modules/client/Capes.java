package uid.infinity.shampoo.features.modules.client;

import uid.infinity.shampoo.features.modules.Module;
import net.minecraft.util.Identifier;

public class Capes extends Module {
    private static Capes INSTANCE = new Capes();
    private final Identifier capeTexture;
    public Capes(){
        super("Cape","Custom capes for you",Category.CLIENT,true,false,false);
        this.setInstance();
        this.capeTexture = Identifier.of("ive","textures/nl.png");
    }

    public static Capes getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Capes();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
    public Identifier getCapeTexture(){
        return capeTexture;
    }
}