package co.netguru.android.inbbbox.feature.details;

public enum ShotDetailsType {
    DEFAULT(0),
    ADD_TO_BUCKET(1),
    LIKES(2),
    BUCKET(3),
    USER(4);

    private final int type;

    ShotDetailsType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
