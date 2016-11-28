package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.buckets.details.BucketDetailsPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface BucketsDetailsComponent {

    void inject(BucketsDetailsComponent bucketsDetailsComponent);

    BucketDetailsPresenter getPresenter();
}
