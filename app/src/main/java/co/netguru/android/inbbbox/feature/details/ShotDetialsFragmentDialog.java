package co.netguru.android.inbbbox.feature.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class ShotDetialsFragmentDialog extends BottomSheetDialogFragment {

    @BindView(R.id.shot_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.parallax_image_view)
    ImageView parallaxImageView;

    private ShotDetailsFragmentCallback callback;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback
            = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
//            verifyBottomSheetState(newState);
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //no-op
        }
    };
    private BottomSheetBehavior behavior;

    public static ShotDetialsFragmentDialog newInstnace(int shotId) {
        return new ShotDetialsFragmentDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater
                .from(getContext())
                .inflate(R.layout.fragment_shot_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        behavior = callback.getBottomSheetBehavior();
        behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        ButterKnife.bind(this, view);

        showMainImage("https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306");
    }

    public void showMainImage(String imageUrl) {
        Glide.with(getContext())
                .load(imageUrl)
                .into(parallaxImageView);
    }

//    @Override
//    public int show(FragmentTransaction transaction, String tag) {
//        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        return super.show(transaction, tag);
//
//    }

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
