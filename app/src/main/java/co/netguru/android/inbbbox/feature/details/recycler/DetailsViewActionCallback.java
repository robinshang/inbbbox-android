package co.netguru.android.inbbbox.feature.details.recycler;

public interface DetailsViewActionCallback {

    void onCompanySelected(String companyProfileUrl);

    void onUserSelected(int userId);

    void onShotLikeAction(int shotId, boolean isLiked);

    void onShotBucket(int shotId, boolean isLikedBucket);
}
