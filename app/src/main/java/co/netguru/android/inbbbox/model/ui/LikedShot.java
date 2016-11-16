package co.netguru.android.inbbbox.model.ui;

import co.netguru.android.inbbbox.model.api.LikedShotEntity;

public class LikedShot implements ShotImage{

    private final int id;
    private final String imageUrl;
    private final boolean isGif;
    private String thumbnailUrl;
    private String hiDpiUrl;


    public LikedShot(int id, String imageUrl, boolean isGif) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.isGif = isGif;
    }

    public LikedShot(LikedShotEntity likedShotEntity) {
        this.id = likedShotEntity.shot().getId();
        this.imageUrl = likedShotEntity.shot().getImage().normalUrl();
        this.isGif = likedShotEntity.shot().getAnimated();
    }

    public int getId() {
        return id;
    }

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

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setHiDpiUrl(String hiDpiUrl) {
        this.hiDpiUrl = hiDpiUrl;
    }
}
