package co.netguru.android.inbbbox.feature.onboarding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.feature.onboarding.recycler.OnboardingLinearLayoutManager;
import co.netguru.android.inbbbox.feature.onboarding.recycler.OnboardingShotsAdapter;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;

public class OnboardingFragment extends BaseMvpFragment<OnboardingContract.View, OnboardingContract.Presenter>
        implements OnboardingContract.View, OnboardingShotSwipeListener {

    @BindView(R.id.shots_recycler_view)
    CustomRecyclerView shotsRecyclerView;

    @BindView(R.id.scroll_overlay)
    View scrollOverlay;

    private OnboardingShotsAdapter adapter;
    private OnboardingLinearLayoutManager layoutManager;

    public static OnboardingFragment newInstance() {
        return new OnboardingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @NonNull
    @Override
    public OnboardingContract.Presenter createPresenter() {
        return App.getUserComponent(getContext()).getOnboardingComponent().getPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
        getPresenter().getShots();
    }

    private void initRecycler() {
        adapter = new OnboardingShotsAdapter(this);
        shotsRecyclerView.setAdapter(adapter);

        layoutManager = new OnboardingLinearLayoutManager(getContext());
        shotsRecyclerView.setLayoutManager(layoutManager);
        shotsRecyclerView.setHasFixedSize(true);

        shotsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrollOverlay.setVisibility(View.GONE);
                    layoutManager.setCanScroll(false);
                }
            }
        });
    }

    public List<OnboardingShot> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<OnboardingShot> data) {
        adapter.setItems(data);
    }

    @Override
    public void onShotLikeSwipe(OnboardingShot shot) {
        getPresenter().handleLikeShot(shot);
    }

    @Override
    public void onAddShotToBucketSwipe(OnboardingShot shot) {
        getPresenter().handleAddShotToBucket(shot);
    }

    @Override
    public void onCommentShotSwipe(OnboardingShot shot) {
        getPresenter().handleCommentShot(shot);
    }

    @Override
    public void onFollowUserSwipe(OnboardingShot shot) {
        getPresenter().handleFollowShotAuthor(shot);
    }

    @Override
    public void onShotSelected(OnboardingShot shot) {
        getPresenter().handleShowShotDetails(shot);
    }

    @Override
    public void scrollToStep(int step) {
        scrollOverlay.setVisibility(View.VISIBLE);
        layoutManager.setCanScroll(true);
        shotsRecyclerView.smoothScrollToPosition(step);
    }
}