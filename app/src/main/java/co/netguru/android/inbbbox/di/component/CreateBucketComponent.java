package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.buckets.createbucket.CreateBucketDialogFragment;
import co.netguru.android.inbbbox.feature.buckets.createbucket.CreateBucketPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface CreateBucketComponent {
    void inject(CreateBucketDialogFragment dialogFragment);

    CreateBucketPresenter getPresenter();
}
