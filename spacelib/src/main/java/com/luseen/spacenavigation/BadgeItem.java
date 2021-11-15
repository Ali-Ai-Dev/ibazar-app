package com.luseen.spacenavigation;

import java.io.Serializable;

class BadgeItem implements Serializable {
    private static final int BADGE_TEXT_MAX_NUMBER = 9;
    private int badgeColor;
    private int badgeIndex;
    private int badgeText;

    BadgeItem(int badgeIndex, int badgeText, int badgeColor) {
        this.badgeIndex = badgeIndex;
        this.badgeText = badgeText;
        this.badgeColor = badgeColor;
    }

    int getBadgeIndex() {
        return this.badgeIndex;
    }

    int getBadgeColor() {
        return this.badgeColor;
    }

    int getIntBadgeText() {
        return this.badgeText;
    }

    String getFullBadgeText() {
        return String.valueOf(this.badgeText);
    }

    String getBadgeText() {
        if (this.badgeText > 9) {
            return "9+";
        }
        return String.valueOf(this.badgeText);
    }
}
