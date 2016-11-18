package co.netguru.android.inbbbox.feature.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import co.netguru.android.inbbbox.view.RoundedCornersImageView;

public class ShotDetailsFragment
        extends BaseMvpFragment<ShotDetailsContract.View, ShotDetailsContract.Presenter>
        implements ShotDetailsContract.View {

    public static final String TAG = ShotDetailsFragment.class.getSimpleName();
    private static final String ARG_SHOT = "arg:shot";
    private ShotDetailsComponent component;

    @BindView(R.id.shot_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.shot_details_recyclerView)
    RecyclerView shotRecyclerView;

    @BindView(R.id.parallax_image_view)
    RoundedCornersImageView parallaxImageView;

    @BindDimen(R.dimen.shot_corner_radius)
    int radius;

    @Inject
    ShotDetailsAdapter adapter;

    private DetailsViewActionCallback actionsCallback = new DetailsViewActionCallback() {
        @Override
        public void onCompanySelected(String companyProfileUrl) {
            // TODO: 15.11.2016 not in scope of this task
            Toast.makeText(getContext(), "company: " + companyProfileUrl, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUserSelected(long userId) {
            // TODO: 15.11.2016 not in scope of this task
            Toast.makeText(getContext(), "userId " + userId, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShotLikeAction(long shotId, boolean isLiked) {
            // TODO: 15.11.2016 not in scope of this task
            Toast.makeText(getContext(), "like: " + isLiked, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShotBucket(long shotId, boolean isLikedBucket) {
            // TODO: 15.11.2016 not in scope of this task
            Toast.makeText(getContext(), "bucket: " + isLikedBucket, Toast.LENGTH_SHORT).show();
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
        Shot shot = getArguments().getParcelable(ARG_SHOT);
        component = App.getAppComponent(getContext())
                .plus(new ShotsDetailsModule(shot, actionsCallback));
        component.inject(this);
    }

    @Override
    public ShotDetailsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getPresenter().downloadData();
    }

    public void initView() {
        shotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotRecyclerView.setAdapter(adapter);
    }

    // TODO: 16.11.2016 it will be public when MVP will be implemented in task IA-146
    public void showDetails(Shot details) {
        adapter.setDetails(details);
    }

    public void showComments(List<Comment> commentList) {
        adapter.setComments(commentList);
    }

    public void showMainImage(ShotImage shotImage) {
        parallaxImageView.setRadius(radius);
        parallaxImageView.disableRadiusForBottomEdge(true);
        ShotLoadingManager.loadMainViewShot(getContext(), parallaxImageView, shotImage);
    }
}
