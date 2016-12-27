package co.netguru.android.inbbbox.bucket.detail;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface BucketsDetailsComponent {

    void inject(BucketsDetailsComponent bucketsDetailsComponent);

    BucketDetailsPresenter getPresenter();
}
