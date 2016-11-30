package co.netguru.android.inbbbox.model.ui;

import co.netguru.android.inbbbox.model.api.LikedShotEntity;

public class LikedShot implements ShotImage {

    private final long id;
    private final String imageUrl;
    private final boolean isGif;
    private final String thumbnailUrl;
    private final String hiDpiUrl;

    public LikedShot(LikedShotEntity likedShotEntity) {
        this.id = likedShotEntity.shot().id();
        this.imageUrl = likedShotEntity.shot().image().normalImageUrl();
        this.isGif = likedShotEntity.shot().animated();
        this.thumbnailUrl = likedShotEntity.shot().image().thumbnailUrl();
        this.hiDpiUrl = likedShotEntity.shot().image().hiDpiImageUrl();
    }

    public long getId() {
        return id;
    }

    public boolean isGif() {
        return isGif;
    }

    @Override
    public String hiDpiImageUrl() {
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