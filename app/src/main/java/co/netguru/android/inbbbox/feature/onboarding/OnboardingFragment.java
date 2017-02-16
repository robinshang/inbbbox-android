package co.netguru.android.inbbbox.feature.onboarding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.feature.main.MainActivity;
import co.netguru.android.inbbbox.feature.onboarding.recycler.OnboardingLinearLayoutManager;
import co.netguru.android.inbbbox.feature.onboarding.recycler.OnboardingShotsAdapter;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.shared.view.AutoItemScrollRecyclerView;

public class OnboardingFragment
        extends BaseMvpFragment<OnboardingContract.View, OnboardingContract.Presenter>
        implements OnboardingContract.View, OnboardingShotSwipeListener {

    @BindView(R.id.shots_recycler_view)
    AutoItemScrollRecyclerView shotsRecyclerView;

    @BindView(R.id.scroll_overlay)
    View scrollOverlay;

    @Inject
    OnboardingShotsAdapter adapter;

    @Inject
    OnboardingLinearLayoutManager layoutManager;

    private OnboardingComponent component;

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
        return component.getPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
        getPresenter().getShots();
    }

    public List<OnboardingStep> getData() {
        return adapter.getData();
    }

    @Override
    public void setData(List<OnboardingStep> data) {
        adapter.setItems(data);
    }

    @Override
    public void closeOnboarding() {
        MainActivity.startActivityWithRequest(getContext(), MainActivity.REQUEST_RESTART);
    }

    @Override
    public void onShotLikeSwipe(OnboardingStep step) {
        getPresenter().handleLikeShot(step);
    }

    @Override
    public void onAddShotToBucketSwipe(OnboardingStep step) {
        getPresenter().handleAddShotToBucket(step);
    }

    @Override
    public void onCommentShotSwipe(OnboardingStep step) {
        getPresenter().handleCommentShot(step);
    }

    @Override
    public void onFollowUserSwipe(OnboardingStep step) {
        getPresenter().handleFollowShotAuthor(step);
    }

    @Override
    public void onSkip(OnboardingStep step) {
        getPresenter().handleSkipFollow(step);
    }

    @Override
    public void onShotSelected(OnboardingStep shot) {
        getPresenter().handleShowShotDetails(shot);
    }

    @Override
    public void scrollToStep(int step) {
        scrollOverlay.setVisibility(View.VISIBLE);
        layoutManager.setCanScroll(true);
        shotsRecyclerView.smoothScrollToPosition(step);
    }

    private void initComponent() {
        component = App.getUserComponent(getContext())
                .plusOnboardingComponent(new OnboardingModule(this, getContext()));
        component.inject(this);
    }

    private void initRecycler() {
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(layoutManager);
        shotsRecyclerView.setHasFixedSize(true);
        shotsRecyclerView.addOnScrollListener(createListener());
    }

    private RecyclerView.OnScrollListener createListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                updateScrollState(newState);
            }
        };
    }

    private void updateScrollState(int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            scrollOverlay.setVisibility(View.GONE);
            layoutManager.setCanScroll(false);
        }
    }
}