package co.netguru.android.inbbbox.feature.bucket.detail;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface BucketsDetailsComponent {

    void inject(BucketDetailsFragment bucketDetailsFragment);

    BucketDetailsPresenter getPresenter();
}
