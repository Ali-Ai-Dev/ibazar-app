package com.luseen.spacenavigation;

import java.io.Serializable;

public class SpaceItem implements Serializable {
    private int itemIcon;
    private String itemName;

    public SpaceItem(String itemName, int itemIcon) {
        this.itemName = itemName;
        this.itemIcon = itemIcon;
    }

    String getItemName() {
        return this.itemName;
    }

    void setItemName(String itemName) {
        this.itemName = itemName;
    }

    int getItemIcon() {
        return this.itemIcon;
    }

    void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }
}
