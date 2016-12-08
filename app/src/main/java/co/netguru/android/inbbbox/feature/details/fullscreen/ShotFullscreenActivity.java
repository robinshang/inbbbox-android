package co.netguru.android.inbbbox.feature.details.fullscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.utils.ShotLoadingManager;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShotFullscreenActivity extends BaseActivity implements RequestListener {

    public static final String KEY_SHOT = "key:shot";

    public static void startActivity(Context context, Shot shot) {
        Intent intent = new Intent(context, ShotFullscreenActivity.class);
        intent.putExtra(KEY_SHOT, shot);
        context.startActivity(intent);
    }

    @BindView(R.id.shot_fullscreen_image)
    ImageView shotImageView;

    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_fullscreen);

        Shot shot = getIntent().getExtras().getParcelable(KEY_SHOT);
        ShotLoadingManager.loadMainViewShotWithListener(this, shotImageView, shot, this);
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target,
                                   boolean isFromMemoryCache, boolean isFirstResource) {
        attacher = new PhotoViewAttacher(shotImageView);
        return false;
    }
}
