package uid.infinity.shampoo.event.impl;

import uid.infinity.shampoo.event.*;

@Cancelable
public class PlayerListColumnsEvent extends Event
{
    private int tabHeight;

    public void setTabHeight(int tabHeight)
    {
        this.tabHeight = tabHeight;
    }

    public int getTabHeight()
    {
        return tabHeight;
    }
}
