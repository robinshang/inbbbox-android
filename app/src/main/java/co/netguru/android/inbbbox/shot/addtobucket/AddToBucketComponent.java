package co.netguru.android.inbbbox.shot.addtobucket;

import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface AddToBucketComponent {

    void inject(AddToBucketDialogFragment addToBucketDialogFragment);

    AddToBucketPresenter getPresenter();
}
