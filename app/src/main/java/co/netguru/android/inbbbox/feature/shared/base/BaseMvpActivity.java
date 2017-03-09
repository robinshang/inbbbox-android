package co.netguru.android.inbbbox.feature.shared.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.shared.base.bottomsheet.BottomSheetActivityCallback;
import co.netguru.android.inbbbox.feature.shared.base.bottomsheet.HidingBottomSheetBearer;

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpActivity<V, P> implements BottomSheetActivityCallback, HidingBottomSheetBearer {

    @Nullable
    @BindView(R.id.bottom_sheet_background)
    View bottomSheetBackground;

    private BaseActivityProxy proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        proxy = new BaseActivityProxy(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        proxy.setContentView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        proxy.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        proxy.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        proxy.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        proxy.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!proxy.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public FragmentTransaction replaceFragment(int containerViewId, Fragment fragment, String tag) {
        return proxy.replaceFragment(containerViewId, fragment, tag);
    }

    protected void showTextOnSnackbar(String text) {
        proxy.showTextOnSnackbar(text);
    }

    @Override
    public void showBottomSheet(Fragment fragment, String tag) {
        proxy.showBottomSheet(fragment, tag);
    }

    @Override
    public void onBottomSheetSlide(float slideOffset) {
        if (bottomSheetBackground != null) {
            bottomSheetBackground.setAlpha(slideOffset);
        }
    }
}