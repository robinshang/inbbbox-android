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
import android.view.animation.AccelerateInterpolator;

import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.di.component.ShotsComponent;
import co.netguru.android.inbbbox.di.module.ShotsModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;
import co.netguru.android.inbbbox.utils.Constants;
import co.netguru.android.inbbbox.view.AutoItemScrollRecyclerView;

public class ShotsFragment
        extends BaseMvpFragment<ShotsContract.View, ShotsContract.Presenter>
        implements ShotsContract.View, ShotsAdapter.OnItemLeftSwipeListener {

    @BindView(R.id.shots_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    @BindView(R.id.container_fog_view)
    View fogContainerView;

    @OnClick(R.id.fab_like_menu)
    void onLikeFabClick() {
        getPresenter().likeShot(shotsRecyclerView.getCurrentItem());
    }

    @Inject
    ShotsAdapter adapter;

    private ShotsComponent component;
    private ShotLikeStatusListener shotLikeStatusListener;

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
                .plus(new ShotsModule());
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
        fabMenu.setOnMenuToggleListener(this::handleFabMenuToggle);
    }

    private void handleFabMenuToggle(boolean opened) {
        if (opened) {
            showFog();
        } else {
            hideFog();
        }
    }

    private void hideFog() {
        fogContainerView
                .animate()
                .alpha(0)
                .setDuration(Constants.Animations.FOG_ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(() -> fogContainerView.setVisibility(View.GONE));
    }

    private void showFog() {
        fogContainerView
                .animate()
                .alpha(1)
                .setDuration(Constants.Animations.FOG_ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .withStartAction(() -> fogContainerView.setVisibility(View.VISIBLE));
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.accent));
        swipeRefreshLayout.setOnRefreshListener(getPresenter()::loadData);
    }

    private void initRecycler() {
        adapter.setOnLeftSwipeListener(this);
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
    public void onItemLeftSwipe(int itemPosition) {
        getPresenter().likeShot(itemPosition);
    }

    public interface ShotLikeStatusListener {
        void shotLikeStatusChanged();
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
