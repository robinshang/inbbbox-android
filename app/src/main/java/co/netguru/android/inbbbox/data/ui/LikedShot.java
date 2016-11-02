package co.netguru.android.inbbbox.data.ui;

public class LikedShot {

    private final int id;
    private final String imageUrl;

    public LikedShot(int id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
