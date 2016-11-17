package co.netguru.android.inbbbox.feature.details.recycler;

public interface DetailsViewActionCallback {

    void onCompanySelected(String companyProfileUrl);

    void onUserSelected(int userId);

    void onShotLikeAction(long shotId, boolean isLiked);

    void onShotBucket(long shotId, boolean isLikedBucket);
}
