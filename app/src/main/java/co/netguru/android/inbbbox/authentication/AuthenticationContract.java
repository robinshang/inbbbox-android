/*
 * Created by Maciej Markiewicz
 * Copyright (c) 2016.
 * netguru.co
 *
 *
 */

package co.netguru.android.inbbbox.authentication;

import android.net.Uri;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface AuthenticationContract {
    interface View extends MvpView{

        void sendActionIntent(Uri uri);
    }

    interface Presenter extends MvpPresenter<View>{
        void showLoginView();
    }
}
