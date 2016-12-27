package co.netguru.android.inbbbox.feature.bucket;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface BucketsFragmentComponent {

    void inject(BucketsFragment bucketsFragment);

    BucketsFragmentPresenter getPresenter();
}
