package co.netguru.android.inbbbox.controler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShotsPagingController {

    public static final int SHOTS_PER_PAGE = 15;

    private int shotsPage = 1;
    private boolean hasMore = true;

    @Inject
    public ShotsPagingController() {

    }

    public int getShotsPage() {
        return shotsPage;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void changeHasMoreToFalse() {
        hasMore = false;
    }

    public void setFirstShotsPage() {
        hasMore = true;
        shotsPage = 1;
    }

    public void setNextShotsPage() {
        shotsPage++;
    }
}
