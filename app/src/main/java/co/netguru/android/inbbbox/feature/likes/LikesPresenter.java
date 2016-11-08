package co.netguru.android.inbbbox.feature.likes;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.controler.LikedShotsController;
import co.netguru.android.inbbbox.model.ui.LikedShot;
import timber.log.Timber;

import static co.netguru.android.commons.rx.RxTransformers.androidIO;

@FragmentScope
public final class LikesPresenter extends MvpNullObjectBasePresenter<LikesViewContract.View>
        implements LikesViewContract.Presenter {

    private final LikedShotsController likedShotsController;

    @Inject
    LikesPresenter(LikedShotsController likedShotsController) {
        this.likedShotsController = likedShotsController;
    }

    @Override
    public void getLikesFromServer() {
        likedShotsController.getLikedShots()
                .toList()
                .compose(androidIO())
                .subscribe(this::onGetLikeShotListNext,
                        throwable -> Timber.e(throwable, "Error while getting likes from server"));
    }

    @Override
    public void addIconToText(String text, Drawable icon) {
        final int index = text.indexOf(' ');
        final ImageSpan imageSpan = new ImageSpan(icon, ImageSpan.ALIGN_BASELINE);
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text.substring(0 , index + 1))
                .append(" ")
                .setSpan(imageSpan, builder.length() - 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(text.substring(index));
        getView().setEmptyViewText(builder);
    }

    private void onGetLikeShotListNext(List<LikedShot> likedShotList) {
        if (likedShotList.isEmpty()) {
            getView().showEmptyLikesInfo();
            return;
        }
        getView().hideEmptyLikesInfo();
        getView().showLikes(likedShotList);
    }
}
