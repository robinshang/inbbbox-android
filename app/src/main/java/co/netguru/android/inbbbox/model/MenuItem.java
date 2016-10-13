package co.netguru.android.inbbbox.model;

import android.annotation.SuppressLint;

import co.netguru.android.inbbbox.R;

public enum MenuItem {

    // TODO: 13.10.2016 Change menu item icons, add selectors
    SHOTS(0, R.drawable.ic_timeline_white_18dp),
    LIKES(1, R.drawable.ic_timeline_white_18dp),
    BUCKETS(2, R.drawable.ic_timeline_white_18dp),
    FOLLOWERS(3, R.drawable.ic_timeline_white_18dp);

    private final int position;
    private final int icon;

    MenuItem(int position, int icon) {
        this.position = position;
        this.icon = icon;
    }

    @SuppressLint("DefaultLocale")
    public static MenuItem getMenuItemForPosition(int position) {
        for (final MenuItem item : values()) {
            if (item.position == position) {
                return item;
            }
        }
        throw new IllegalArgumentException(
                String.format("There is no menuItem defined for position: %d", position));
    }

    public int getPosition() {
        return position;
    }

    public int getIcon() {
        return icon;
    }
}
