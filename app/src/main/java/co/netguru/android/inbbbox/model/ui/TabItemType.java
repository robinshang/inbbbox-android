package co.netguru.android.inbbbox.model.ui;

import android.annotation.SuppressLint;

import co.netguru.android.inbbbox.R;

public enum TabItemType {

    SHOTS(0, R.drawable.ic_shots, R.string.tab_shots_title),
    LIKES(1, R.drawable.ic_likes, R.string.tab_likes_title),
    BUCKETS(2, R.drawable.ic_buckets, R.string.tab_buckets_title),
    FOLLOWERS(3, R.drawable.ic_following, R.string.tab_following_title);

    private final int position;
    private final int icon;
    private final int title;

    TabItemType(int position, int icon, int title) {
        this.position = position;
        this.icon = icon;
        this.title = title;
    }

    @SuppressLint("DefaultLocale")
    public static TabItemType getTabItemForPosition(int position) {
        for (final TabItemType item : values()) {
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

    public int getTitle() {
        return title;
    }
}
