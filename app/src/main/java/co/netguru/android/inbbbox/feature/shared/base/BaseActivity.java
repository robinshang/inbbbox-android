package co.netguru.android.inbbbox.feature.shared.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import co.netguru.android.inbbbox.feature.shared.base.bottomsheet.BottomSheetActivityCallback;
import co.netguru.android.inbbbox.feature.shared.base.bottomsheet.HidingBottomSheetBearer;

public abstract class BaseActivity extends AppCompatActivity
        implements BottomSheetActivityCallback, HidingBottomSheetBearer {

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

    protected void showTextOnSnackbar(@StringRes int stringRes) {
        proxy.showTextOnSnackbar(stringRes);
    }

    @Override
    public void showBottomSheet(Fragment fragment, String tag) {
        proxy.showBottomSheet(fragment, tag);
    }

    @Override
    public void onBottomSheetSlide(float slideOffset) {
        //no-op
    }
}