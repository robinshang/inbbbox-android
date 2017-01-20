package co.netguru.android.inbbbox.feature.shot.removefrombucket;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface RemoveFromBucketComponent {

    void inject(RemoveFromBucketDialogFragment removeFromBucketDialogFragment);

    RemoveFromBucketPresenter getPresenter();
}
