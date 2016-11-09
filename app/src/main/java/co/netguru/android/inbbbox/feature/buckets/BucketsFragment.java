package co.netguru.android.inbbbox.feature.buckets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;

public class BucketsFragment extends BaseMvpFragmentWithWithListTypeSelection {

    public static BucketsFragment newInstance() {
        return new BucketsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buckets, container, false);
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        //// TODO: 09.11.2016 not in scope of this task
    }

    @Override
    public MvpPresenter createPresenter() {
        return App.getAppComponent(getContext()).inject().getPresenter();
    }
}
