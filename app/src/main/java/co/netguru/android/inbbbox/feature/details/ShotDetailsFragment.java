package co.netguru.android.inbbbox.feature.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.ShotDetailsComponent;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.details.recycler.DetailsViewActionCallback;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.ShotImage;
import co.netguru.android.inbbbox.model.ui.Team;
import co.netguru.android.inbbbox.model.ui.User;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import co.netguru.android.inbbbox.utils.ViewAnimator;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;
import timber.log.Timber;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class ShotDetailsFragment
        extends BaseMvpFragment<ShotDetailsContract.View, ShotDetailsContract.Presenter>
        implements ShotDetailsContract.View {

    public static final String TAG = ShotDetailsFragment.class.getSimpleName();
    private static final String ARG_SHOT = "arg:shot";

    @BindView(R.id.shot_details_recyclerView)
    RecyclerView shotRecyclerView;

    @BindView(R.id.parallax_image_view)
    RoundedCornersImageView parallaxImageView;

    @BindView(R.id.comment_input_panel)
    View shotCommentInputPanel;

    @BindView(R.id.comment_textInputLayout)
    TextInputLayout commentTextInputLayout;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindDimen(R.dimen.shot_corner_radius)
    int radius;

    private ShotDetailsComponent component;
    private LinearLayoutManager linearLayoutManager;
    private boolean isInputPanelShowingEnabled;

    @OnClick(R.id.comment_send_ImageView)
    void onSendCommentClick() {
        getPresenter().sendComment();
        Toast.makeText(getContext(), getCommentText(), Toast.LENGTH_SHORT).show();
    }

    @Inject
    ShotDetailsAdapter adapter;

    private DetailsViewActionCallback actionsCallback = new DetailsViewActionCallback() {

        @Override
        public void onTeamSelected(Team team) {
            // TODO: 15.11.2016 not in scope of this task
            Toast.makeText(getContext(), "team clicked: " + team.name(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUserSelected(User user) {
            // TODO: 15.11.2016 not in scope of this task
            Toast.makeText(getContext(), "user clicked " + user.name(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShotLikeAction(boolean newLikeState) {
            getPresenter().handleShotLike(newLikeState);
        }

        @Override
        public void onShotBucket(long shotId, boolean isLikedBucket) {
            // TODO: 15.11.2016 not in scope of this task
            Toast.makeText(getContext(), "bucket: " + isLikedBucket, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoadMoreCommentsSelected() {
            Toast.makeText(getContext(), "LOAD MORE!!", Toast.LENGTH_SHORT).show();
        }

    };

    @OnClick(R.id.details_close_imageView)
    void onCloseClick() {
        getActivity().onBackPressed();
    }

    public static ShotDetailsFragment newInstance(Shot shot) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_SHOT, shot);
        ShotDetailsFragment fragment = new ShotDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initComponent();
        return LayoutInflater
                .from(getContext())
                .inflate(R.layout.fragment_shot_details, container, false);
    }

    private void initComponent() {
        component = App.getAppComponent(getContext())
                .plus(new ShotsDetailsModule(actionsCallback));
        component.inject(this);
    }

    @NonNull
    @Override
    public ShotDetailsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getPresenter().retrieveInitialData();
        getPresenter().downloadData();
    }

    @Override
    public void initView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        shotRecyclerView.setLayoutManager(linearLayoutManager);
        shotRecyclerView.addOnScrollListener(createScrollListener());
        shotRecyclerView.setAdapter(adapter);
        appBarLayout.addOnOffsetChangedListener((layout, offset) -> verifyInputVisibility());
    }

    private RecyclerView.OnScrollListener createScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                verifyInputVisibility();
            }
        };
    }

    private void verifyInputVisibility() {
        int lastVisibleIndex = linearLayoutManager.findLastVisibleItemPosition();
        if (isInputPanelShowingEnabled && adapter.isInputVisibilityPermitted(lastVisibleIndex)) {
            showInputIfHidden();
        }
    }

    @Override
    public void showErrorMessage(String errorMessageLabel) {
        Toast.makeText(getContext(), errorMessageLabel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Shot getShotInitialData() {
        return getArguments().getParcelable(ARG_SHOT);
    }

    @Override
    public String getCommentText() {
        return commentTextInputLayout.getEditText().getText().toString();
    }

    @Override
    public void setInputShowingEnabled(boolean isInputPanelShowingEnabled) {

        this.isInputPanelShowingEnabled = isInputPanelShowingEnabled;
    }

    @Override
    public void showDetails(Shot details) {
        adapter.setDetails(details);
    }

    @Override
    public void showComments(List<Comment> commentList) {
        verifyInputVisibility();
        adapter.setComments(commentList);
    }

    @Override
    public void showMainImage(ShotImage shotImage) {
        parallaxImageView.setRadius(radius);
        parallaxImageView.disableRadiusForBottomEdge(true);
        ShotLoadingManager.loadMainViewShot(getContext(), parallaxImageView, shotImage);
    }

    private void showInputIfHidden() {
        if (shotCommentInputPanel.getVisibility() == View.GONE) {
            ViewAnimator.startSlideInAnimation(shotCommentInputPanel);
        }
    }
}
