package co.netguru.android.inbbbox.data.ui;

public class Shot implements ImageThumbnail {
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public String getThumbnailUrl() {
        return null;
    }

    @Override
    public Integer getPlaceholderResId() {
        return null;
    }

    @Override
    public Integer getErrorImageResId() {
        return null;
    }
}
