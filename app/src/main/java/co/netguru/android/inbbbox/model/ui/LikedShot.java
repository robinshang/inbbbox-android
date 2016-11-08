package co.netguru.android.inbbbox.model.ui;

import co.netguru.android.inbbbox.model.api.LikedShotEntity;

public class LikedShot {

    private final int id;
    private final String imageUrl;

    public LikedShot(int id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public LikedShot(LikedShotEntity likedShotEntity) {
        this.id = likedShotEntity.shot().getId();
        this.imageUrl = likedShotEntity.shot().getImage().normalUrl();
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
