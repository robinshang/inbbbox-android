package co.netguru.android.inbbbox.model.ui;

import co.netguru.android.inbbbox.model.api.LikedShotEntity;

public class LikedShot implements ShotImage {

    private final int id;
    private final String imageUrl;
    private final boolean isGif;
    private final String thumbnailUrl;
    private final String hiDpiUrl;

    public LikedShot(LikedShotEntity likedShotEntity) {
        this.id = likedShotEntity.shot().getId();
        this.imageUrl = likedShotEntity.shot().getImage().normalUrl();
        this.isGif = likedShotEntity.shot().getAnimated();
        this.thumbnailUrl = likedShotEntity.shot().getImage().teaserUrl();
        this.hiDpiUrl = likedShotEntity.shot().getImage().hiDpiUrl();
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean isGif() {
        return isGif;
    }

    @Override
    public String hdpiImageUrl() {
        return this.hiDpiUrl;
    }

    @Override
    public String normalImageUrl() {
        return this.imageUrl;
    }

    @Override
    public String thumbnailUrl() {
        return this.thumbnailUrl;
    }
}
