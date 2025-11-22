package uid.infinity.shampoo.features.modules.misc;

import com.google.common.eventbus.Subscribe;
import uid.infinity.shampoo.event.impl.PacketEvent;
import uid.infinity.shampoo.features.modules.Module;
import uid.infinity.shampoo.features.settings.Setting;

public class AntiSpam extends Module {
    public Setting<Boolean> links = this.bool("Links", true);
    public Setting<Boolean> anc = this.bool("Annoucers", true);
    public Setting<Boolean> clan = this.bool("CMKClan", true);
    private final String[] chat = new String[]{"hhtps:", ".ru", ".com", ".net", ".me", ".org", ".xyz"};
    private final String[] annoucers = new String[]{"I just", "I moved", "I placed"};
    private final String[] clanspam = new String[]{"Привет, хочешь в клан? Именно сейчас проходит набор в CMK! За информацией пишите в дс: nolikcpvp"};

    public AntiSpam() {
        super("AntiSpam", "Blocks spam messages such as links, announcer texts, and clan advertisements", Category.MISC, true, false, false);
    }

    @Subscribe
    public void onPacket(PacketEvent.Receive event) {
        if (this.links.getValue()) {
            for (String text : this.chat) {
                if (event.getPacket().equals(text)) event.cancel();
            }

            if (this.anc.getValue()) {
                for (String message : this.annoucers) {
                    if (event.getPacket().equals(message)) event.cancel();
                }
            }

            if (this.clan.getValue()) {
                for (String messages : this.clanspam) {
                    if (event.getPacket().equals(messages)) event.cancel();
                }
            }
        }
    }
}

