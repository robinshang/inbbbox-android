package co.netguru.android.inbbbox.feature.shared;

import android.annotation.SuppressLint;

import co.netguru.android.inbbbox.R;

public enum UserDetailsTabItemType {

    SHOTS(0, R.string.tab_shots_title),
    INFO(1, R.string.tab_info_title),
    PROJECTS(2, R.string.tab_projects_title),
    BUCKETS(3, R.string.tab_buckets_title);

    private final int position;
    private final int title;

    UserDetailsTabItemType(int position, int title) {
        this.position = position;
        this.title = title;
    }

    @SuppressLint("DefaultLocale")
    public static UserDetailsTabItemType getTabItemForPosition(int position) {
        for (final UserDetailsTabItemType item : values()) {
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

    public int getTitle() {
        return title;
    }
}
