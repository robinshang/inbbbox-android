package co.netguru.android.inbbbox.data.ui;

import co.netguru.android.inbbbox.R;

public class Shot implements ImageThumbnail {
    private Integer id;
    private String title;
    private String description;
    private String hdpiImage;
    private String normalImage;
    private String thumbnail;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getImageUrl() {
        return getNormalImage();
    }

    @Override
    public String getThumbnailUrl() {
        return getThumbnail();
    }

    // TODO: 26.10.2016 Replace with suitable placeholder
    @Override
    public Integer getPlaceholderResId() {
        return R.drawable.ic_ball;
    }

    @Override
    public Integer getErrorImageResId() {
        return null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHdpiImage(String hdpiImage) {
        this.hdpiImage = hdpiImage;
    }

    public void setNormalImage(String normalImage) {
        this.normalImage = normalImage;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getHdpiImage() {
        return hdpiImage;
    }

    public String getNormalImage() {
        return normalImage;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
