package co.netguru.android.inbbbox.di.component;

import co.netguru.android.commons.di.FragmentScope;
import co.netguru.android.inbbbox.feature.shots.addtobucket.AddToBucketDialogFragment;
import co.netguru.android.inbbbox.feature.shots.addtobucket.AddToBucketPresenter;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface AddToBucketComponent {

    void inject(AddToBucketDialogFragment addToBucketDialogFragment);

    AddToBucketPresenter getPresenter();
}
