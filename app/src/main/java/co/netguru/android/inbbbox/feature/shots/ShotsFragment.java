package co.netguru.android.inbbbox.feature.shots;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.ShotsComponent;
import co.netguru.android.inbbbox.di.module.ShotsModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.shots.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotSwipeListener;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;
import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.view.AutoItemScrollRecyclerView;
import co.netguru.android.inbbbox.view.FogFloatingActionMenu;

public class ShotsFragment
        extends BaseMvpFragment<ShotsContract.View, ShotsContract.Presenter>
        implements ShotsContract.View, ShotSwipeListener, AddToBucketDialogFragment.BucketSelectListener {

    private ShotsComponent component;
    private ShotLikeStatusListener shotLikeStatusListener;

    @Inject
    ShotsAdapter adapter;

    @BindView(R.id.shots_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_menu)
    FogFloatingActionMenu fabMenu;

    @BindView(R.id.container_fog_view)
    View fogContainerView;

    @OnClick(R.id.fab_like_menu)
    void onLikeFabClick() {
        getPresenter().likeShot(adapter.getShotFromPosition(shotsRecyclerView.getCurrentItem()));
    }

    @OnClick(R.id.fab_bucket_menu)
    void onBucketClick() {
        getPresenter().handleAddShotToBucket(
                adapter.getShotFromPosition(shotsRecyclerView.getCurrentItem()));
    }

    @OnClick(R.id.fab_comment_menu)
    void onCommentClick() {
        // TODO: 07.11.2016 replace this when feature will be implemented
        Toast.makeText(getContext(), "Comment", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_follow_menu)
    void onFollowClick() {
        // TODO: 07.11.2016 replace this when feature will be implemented
        Toast.makeText(getContext(), "Follow", Toast.LENGTH_SHORT).show();
    }

    public static ShotsFragment newInstance() {
        return new ShotsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            shotLikeStatusListener = (ShotLikeStatusListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ShotLikeStatusListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shots, container, false);
    }

    private void initComponent() {
        component = App.getAppComponent(getContext())
                .plus(new ShotsModule(this));
        component.inject(this);
    }

    @Override
    public ShotsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
        initRefreshLayout();
        initFabMenu();
        getPresenter().loadData();
    }

    private void initFabMenu() {
        fabMenu.addFogView(fogContainerView);
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::loadData);
    }

    private void initRecycler() {
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotsRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void showItems(List<Shot> items) {
        adapter.setItems(items);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(shotsRecyclerView, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void changeShotLikeStatus(Shot shot) {
        adapter.changeShotLikeStatus(shot);
        shotLikeStatusListener.shotLikeStatusChanged();
    }

    @Override
    public void closeFabMenu() {
        if (fabMenu.isOpened()) {
            fabMenu.close(true);
        }
    }

    @Override
    public void showBucketChoosing(Shot shot) {
        AddToBucketDialogFragment.newInstance(this, shot)
                .show(getActivity().getSupportFragmentManager(), AddToBucketDialogFragment.TAG);
    }

    @Override
    public void showBucketAddSuccess() {
        showTextOnSnackbar(R.string.shots_fragment_add_shot_to_bucket_success);
    }

    @Override
    public void onShotLikeSwipe(Shot shot) {
        getPresenter().likeShot(shot);
    }

    @Override
    public void onAddShotToBucketSwipe(Shot shot) {
        getPresenter().handleAddShotToBucket(shot);
    }

    @Override
    public void onCommentShotSwipe(Shot shot) {
        //// TODO: 09.11.2016 Implement comment shot callback
        Toast.makeText(getContext(), "todo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBucketForShotSelect(Bucket bucket, Shot shot) {
        getPresenter().addShotToBucket(bucket, shot);
    }

    @FunctionalInterface
    public interface ShotLikeStatusListener {
        void shotLikeStatusChanged();
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
