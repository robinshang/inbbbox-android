package co.netguru.android.inbbbox.feature.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class ShotDetialsFragmentDialog extends Fragment {

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
