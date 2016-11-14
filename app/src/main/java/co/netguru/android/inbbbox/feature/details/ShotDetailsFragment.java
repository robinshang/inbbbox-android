package co.netguru.android.inbbbox.feature.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.ShotDetailsComponent;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import timber.log.Timber;

public class ShotDetailsFragment extends BottomSheetDialogFragment {

    private ShotDetailsFragmentCallback callback;
    private BottomSheetBehavior behavior;
    private ShotDetailsComponent component;

    @BindView(R.id.shot_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.shot_details_recyclerView)
    RecyclerView shotRecyclerView;

    @BindView(R.id.parallax_image_view)
    ImageView parallaxImageView;

    @Inject
    ShotDetailsAdapter adapter;

    public static ShotDetailsFragment newInstance(int shotId) {
        return new ShotDetailsFragment();
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
        component = App.getAppComponent(getContext()).plus(new ShotsDetailsModule());
        component.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        behavior = callback.getBottomSheetBehavior();
        ButterKnife.bind(this, view);

        showMainImage("https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306");
    }

    public void showMainImage(String imageUrl) {
        Glide.with(getContext())
                .load(imageUrl)
                .into(parallaxImageView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (ShotDetailsFragmentCallback) context;
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement ShotDetailsFragmentCallback");
        }
    }
}
