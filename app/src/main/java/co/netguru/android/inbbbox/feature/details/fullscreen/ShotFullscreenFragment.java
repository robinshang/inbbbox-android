package co.netguru.android.inbbbox.feature.details.fullscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import timber.log.Timber;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShotFullscreenFragment extends
        BaseMvpFragment<ShotFullscreenContract.View, ShotFullscreenContract.Presenter> implements
        ShotFullscreenContract.View, RequestListener {

    public static final String TAG = ShotFullscreenFragment.class.getSimpleName();
    public static final String KEY_SHOT = "key:shot";
    public static final String KEY_ALL_SHOTS = "key:all_shots";

    @BindView(R.id.shot_fullscreen_image)
    ImageView shotImageView;

    public static ShotFullscreenFragment newInstance(Shot shot, List<Shot> allShots) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_SHOT, shot);

        if (allShots instanceof ArrayList) {
            args.putParcelableArrayList(KEY_ALL_SHOTS, (ArrayList<Shot>) allShots);
        } else {
            args.putParcelableArrayList(KEY_ALL_SHOTS, new ArrayList<Shot>(allShots));
        }

        ShotFullscreenFragment shotFullscreenFragment = new ShotFullscreenFragment();
        shotFullscreenFragment.setArguments(args);

        return shotFullscreenFragment;
    }

    @Override
    public ShotFullscreenContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusShotFullscreenComponent().getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shot_fullscreen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Shot shot = getArguments().getParcelable(KEY_SHOT);
        List<Shot> allShots = getArguments().getParcelableArrayList(KEY_ALL_SHOTS);
        // TODO create viewpager with shots and lazy loading

        getPresenter().onViewCreated(shot);
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        if (e != null) {
            Timber.e(e, "Error occurred when getting shot image");
        }
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target,
                                   boolean isFromMemoryCache, boolean isFirstResource) {
        new PhotoViewAttacher(shotImageView);
        return false;
    }

    @Override
    public void previewShot(Shot shot) {
        ShotLoadingManager.loadMainViewShotWithListener(getContext(), shotImageView, shot, this);
    }
}
