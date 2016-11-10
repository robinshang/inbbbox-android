package co.netguru.android.inbbbox.feature.shots.addtobucket;


import android.os.Bundle;
import android.support.annotation.NonNull;

import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.feature.common.BaseMvpDialogFragment;
import co.netguru.android.inbbbox.model.ui.Shot;

public class AddToBucketDialogFragment extends BaseMvpDialogFragment<AddToBucketContract.View, AddToBucketContract.Presenter>
        implements AddToBucketContract.View {

    public static final String TAG = AddToBucketDialogFragment.class.getSimpleName();

    private static final String SHOT_ARG_KEY = "shot_arg_key";

    public static AddToBucketDialogFragment newInstance(Shot shot) {

        Bundle args = new Bundle();

        args.putParcelable(SHOT_ARG_KEY, shot);

        AddToBucketDialogFragment fragment = new AddToBucketDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public AddToBucketContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusAddToBucketComponent().getPresenter();
    }
}
