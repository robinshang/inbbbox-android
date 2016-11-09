package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.buckets.BucketsFragment;
import co.netguru.android.inbbbox.feature.buckets.BucketsFragmentPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface BucketsFragmentComponent {

    void inject(BucketsFragment bucketsFragment);

    BucketsFragmentPresenter getPresenter();
}
