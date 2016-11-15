package co.netguru.android.inbbbox.feature.followers.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.FollowerDetailsFragmentComponent;
import co.netguru.android.inbbbox.di.module.FollowerDetailsFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.feature.followers.details.adapter.FollowerDetailsAdapter;
import co.netguru.android.inbbbox.model.ui.Follower;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class FollowerDetailsFragment extends BaseMvpFragmentWithWithListTypeSelection<FollowerDetailsContract.View,
        FollowerDetailsContract.Presenter> implements FollowerDetailsContract.View {

    public static final int GRID_VIEW_COLUMN_COUNT = 2;
    public static final String TAG = FollowerDetailsFragment.class.getSimpleName();
    private static final String FOLLOWER_KEY = "follower_key";
    private static final int SHOTS_TO_LOAD_MORE = 10;

    @BindView(R.id.fragment_follower_details_recycler_view)
    RecyclerView recyclerView;

    @Inject
    FollowerDetailsAdapter adapter;
    @Inject
    GridLayoutManager gridLayoutManager;
    @Inject
    LinearLayoutManager linearLayoutManager;

    private FollowerDetailsFragmentComponent component;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follower_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        getPresenter().followerDataReceived(getArguments().getParcelable(FOLLOWER_KEY));
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        adapter.setGridMode(isGridMode);
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
    }

    @NonNull
    @Override
    public FollowerDetailsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void showFollowerData(Follower follower) {
        adapter.setAdapterData(follower);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(follower.name());
        }
    }

    @Override
    public void showMoreUserShots(List<Shot> shotList) {
        adapter.addMoreUserShots(shotList);
    }

    private void initRecyclerView() {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return GRID_VIEW_COLUMN_COUNT;
                }
                return 1;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreUserShotsFromServer();
            }
        });
    }
}
