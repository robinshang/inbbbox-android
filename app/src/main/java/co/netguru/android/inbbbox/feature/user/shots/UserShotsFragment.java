package co.netguru.android.inbbbox.feature.user.shots;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpViewStateFragment;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPop;
import co.netguru.android.inbbbox.feature.shared.shotsadapter.SharedShotsAdapter;
import co.netguru.android.inbbbox.feature.shared.view.LoadMoreScrollListener;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.user.info.team.ShotActionListener;
import co.netguru.android.inbbbox.feature.user.shots.adapter.UserShotsAdapter;
import timber.log.Timber;

public class UserShotsFragment extends BaseMvpViewStateFragment<SwipeRefreshLayout, List<Shot>,
        UserShotsContract.View, UserShotsContract.Presenter>
        implements UserShotsContract.View, PeekAndPop.OnGeneralActionListener,
        ShotPeekAndPop.ShotPeekAndPopListener, AddToBucketDialogFragment.BucketSelectListener {

    public static final String TAG = UserShotsFragment.class.getSimpleName();

    private static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final String USER_KEY = "user_key";
    private static final int SHOTS_TO_LOAD_MORE = 10;

    @BindColor(R.color.accent)
    int accentColor;

    @BindView(R.id.contentView)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_follower_details_recycler_view)
    RecyclerView recyclerView;

    private ShotActionListener shotActionListener;
    private SharedShotsAdapter adapter;
    private ShotPeekAndPop peekAndPop;

    public static UserShotsFragment newInstance(User user) {
        final Bundle args = new Bundle();
        args.putParcelable(USER_KEY, user);

        final UserShotsFragment fragment = new UserShotsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotActionListener = (ShotActionListener) context;
        } catch (ClassCastException e) {
            Timber.e(e, "must implement OnFollowedShotActionListener");
            throw new InterfaceNotImplementedException(e, context.toString(), ShotActionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        shotActionListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_shots, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPeekAndPop();
        initRefreshLayout();
        initRecyclerView();
    }

    @NonNull
    @Override
    public UserShotsContract.Presenter createPresenter() {
        UserShotsModule module = new UserShotsModule(getArguments().getParcelable(USER_KEY));
        return App.getUserComponent(getContext())
                .plusUserShotsComponent(module).getPresenter();
    }

    @NonNull
    @Override
    public LceViewState<List<Shot>, UserShotsContract.View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Shot> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<Shot> data) {
        adapter.setShots(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().userDataReceived(getArguments().getParcelable(USER_KEY));
    }

    @Override
    public void showMoreUserShots(List<Shot> shotList) {
        adapter.addNewShots(shotList);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void openShotDetailsScreen(Shot shot, List<Shot> allShots, long userId) {
        shotActionListener.showShotDetails(shot, allShots, userId);
    }


    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(recyclerView, errorText, Snackbar.LENGTH_SHORT).show();
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(accentColor);
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::refreshUserShots);
    }

    private void initRecyclerView() {
        adapter = new SharedShotsAdapter(getPresenter()::showShotDetails, peekAndPop);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                GRID_VIEW_COLUMN_COUNT);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(getScrollListener());
    }

    private LoadMoreScrollListener getScrollListener() {
        return new LoadMoreScrollListener(SHOTS_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                getPresenter().getMoreUserShotsFromServer();
            }
        };
    }

    private void initPeekAndPop() {
        peekAndPop = new ShotPeekAndPop(
                new PeekAndPop.Builder(getActivity())
                        .blurBackground(true)
                        .peekLayout(R.layout.peek_shot_details)
                        .parentViewGroupToDisallowTouchEvents(recyclerView));
        peekAndPop.setShotPeekAndPopListener(this);
        peekAndPop.setOnGeneralActionListener(this);
    }

    @Override
    public void onPeek(View view, int i) {
        peekAndPop.bindPeekAndPop(adapter.getData().get(i));
        recyclerView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onPop(View view, int i) {
        // no-op
    }

    @Override
    public void showBucketChooserView(Shot shot) {
        AddToBucketDialogFragment.newInstance(this, shot)
                .show(getActivity().getSupportFragmentManager(), AddToBucketDialogFragment.TAG);
    }

    @Override
    public void showBucketAddSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_add_shot_to_bucket_success);
    }

    @Override
    public void onBucketShot(Shot shot) {
        getPresenter().onBucketShot(shot);
    }

    @Override
    public void onShotLiked() {
        Snackbar.make(getView(), R.string.shot_liked, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onShotUnliked() {
        Snackbar.make(getView(), R.string.shot_unliked, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        getPresenter().addShotToBucket(shot, bucket);
    }
}
