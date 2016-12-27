package co.netguru.android.inbbbox.bucket.createbucket;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface CreateBucketComponent {
    void inject(CreateBucketDialogFragment dialogFragment);

    CreateBucketPresenter getPresenter();
}
