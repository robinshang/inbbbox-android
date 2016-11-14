package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.FollowerDetailsFragmentComponent;
import co.netguru.android.inbbbox.di.module.FollowerDetailsFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.model.ui.Follower;

public class FollowerDetailsFragment extends BaseMvpFragmentWithWithListTypeSelection<FollowerDetailsContract.View,
        FollowerDetailsContract.Presenter> implements FollowerDetailsContract.View {

    public static final String TAG = FollowerDetailsFragment.class.getSimpleName();
    private static final String FOLLOWER_KEY = "follower_key";

    @BindView(R.id.fragment_follower_details_recycler_view)
    RecyclerView recyclerView;

    private FollowerDetailsFragmentComponent component;
    private Follower follower;

    public static FollowerDetailsFragment newInstance(Follower follower) {
        final Bundle args = new Bundle();
        args.putParcelable(FOLLOWER_KEY, follower);

        final FollowerDetailsFragment fragment = new FollowerDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        component = App.getAppComponent(getContext())
                .plus(new FollowerDetailsFragmentModule());
        component.inject(this);
        follower = getArguments().getParcelable(FOLLOWER_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follower_details, container, false);
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        // TODO: 14.11.2016 Fill this method
    }

    @NonNull
    @Override
    public FollowerDetailsContract.Presenter createPresenter() {
        return component.getPresenter();
    }
}
