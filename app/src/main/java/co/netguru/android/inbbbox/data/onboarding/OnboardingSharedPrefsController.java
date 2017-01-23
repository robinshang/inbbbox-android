package co.netguru.android.inbbbox.data.onboarding;

import android.content.SharedPreferences;

import org.threeten.bp.ZonedDateTime;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import rx.Observable;
import rx.Single;

@Singleton
public class OnboardingSharedPrefsController implements OnboardingController {

    public static final String KEY_CURRENT_STEP = "onboarding:current_step";
    public static final int TOTAL_STEPS = 5;

    private final SharedPreferences sharedPreferences;

    @Inject
    public OnboardingSharedPrefsController(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Single<Integer> getCurrentStep() {
        return Single.just(sharedPreferences.getInt(KEY_CURRENT_STEP, 0));
    }

    @Override
    public void setCurrentStep(int step) {
        sharedPreferences.edit().putInt(KEY_CURRENT_STEP, step).apply();
    }

    @Override
    public Observable<List<Shot>> getShots() {
        Shot shot1 = Shot.builder()
                .id(0)
                .creationDate(ZonedDateTime.now())
                .likesCount(0)
                .bucketCount(0)
                .commentsCount(0)
                .isGif(false)
                .isLiked(false)
                .isBucketed(false)
                .build();

        Shot shot2 = Shot.builder()
                .id(0)
                .creationDate(ZonedDateTime.now())
                .likesCount(0)
                .bucketCount(0)
                .commentsCount(0)
                .isGif(false)
                .isLiked(false)
                .isBucketed(false)
                .build();

        Shot shot3 = Shot.builder()
                .id(0)
                .creationDate(ZonedDateTime.now())
                .likesCount(0)
                .bucketCount(0)
                .commentsCount(0)
                .isGif(false)
                .isLiked(false)
                .isBucketed(false)
                .build();

        Shot shot4 = Shot.builder()
                .id(0)
                .creationDate(ZonedDateTime.now())
                .likesCount(0)
                .bucketCount(0)
                .commentsCount(0)
                .isGif(false)
                .isBucketed(false)
                .isLiked(false)
                .build();


        Shot shot5 = Shot.builder()
                .id(0)
                .creationDate(ZonedDateTime.now())
                .likesCount(0)
                .bucketCount(0)
                .commentsCount(0)
                .isGif(false)
                .isBucketed(false)
                .isLiked(false)
                .build();
        return Observable.just(Arrays.asList(shot1, shot2, shot3, shot4));
    }
}
