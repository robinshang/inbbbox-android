package co.netguru.android.inbbbox.feature.buckets.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.model.api.ShotEntity;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;

public class BucketDetailsFragment
        extends BaseMvpFragmentWithWithListTypeSelection<BucketDetailsContract.View, BucketDetailsContract.Presenter> {

    public static final String TAG = BucketDetailsFragment.class.getSimpleName();

    private static final String BUCKET_WITH_SHOTS_ARG_KEY = "bucket_with_shots_arg_key";
    private static final String SHOTS_PER_PAGE_ARG_KEY = "shots_per_page_arg_key";

    @BindView(R.id.shots_from_bucket_recycler_view)
    RecyclerView recyclerView;

    //// TODO: 28.11.2016 implement details view
    public static BucketDetailsFragment newInstance(BucketWithShots bucketWithShots, int perPage) {

        Bundle args = new Bundle();
        args.putParcelable(BUCKET_WITH_SHOTS_ARG_KEY, bucketWithShots);
        args.putInt(SHOTS_PER_PAGE_ARG_KEY, perPage);

        BucketDetailsFragment fragment = new BucketDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bucket_details, container, false);
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {

    }

    @Override
    public BucketDetailsContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusBucketDetailsComponent().getPresenter();
    }

    @FunctionalInterface
    public interface OnShotInBucketClickListener {
        void onShotClick(ShotEntity shotEntity);
    }
}
