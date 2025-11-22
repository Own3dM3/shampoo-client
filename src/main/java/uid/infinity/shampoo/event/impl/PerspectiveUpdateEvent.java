package uid.infinity.shampoo.event.impl;

import uid.infinity.shampoo.event.Event;
import net.minecraft.client.option.Perspective;

public class PerspectiveUpdateEvent extends Event
{
    private final Perspective perspective;

    public PerspectiveUpdateEvent(Perspective perspective)
    {
        this.perspective = perspective;
    }

    public Perspective getPerspective()
    {
        return perspective;
    }
}
