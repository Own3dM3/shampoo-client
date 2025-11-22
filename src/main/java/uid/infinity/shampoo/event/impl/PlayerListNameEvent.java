package uid.infinity.shampoo.event.impl;

import net.minecraft.text.Text;
import uid.infinity.shampoo.event.*;

import java.util.UUID;

@Cancelable
public class PlayerListNameEvent extends Event {
    private Text playerName;
    private final UUID id;

    public PlayerListNameEvent(Text playerName, UUID id)
    {
        this.playerName = playerName;
        this.id = id;
    }

    public void setPlayerName(Text playerName)
    {
        this.playerName = playerName;
    }

    public Text getPlayerName()
    {
        return playerName;
    }

    public UUID getId()
    {
        return id;
    }
}