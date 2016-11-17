package co.netguru.android.inbbbox.feature.details;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import co.netguru.android.inbbbox.model.ui.ShotDetails;
import co.netguru.android.inbbbox.model.ui.ShotImage;

public interface ShotDetailsContract {

    interface View extends MvpView {

        void showDetails(ShotDetails shotDetails);

        void showMainImage(ShotImage shotImage);

        void initView();
    }

    interface Presenter extends MvpPresenter<View> {

        void downloadData();
    }
}
