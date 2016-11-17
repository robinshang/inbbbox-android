package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.model.ui.ShotDetails;

public interface ShotDetailsContract {

    interface View extends MvpView {

        void showItems(ShotDetails shotDetails);

        void showMainImage(String exampleImageUrl, String exampleImageUrl1);
    }

    interface Presenter extends MvpPresenter<View> {

        void downloadData();
    }
}
