package co.netguru.android.inbbbox.feature.projects;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import co.netguru.android.commons.di.FragmentScope;

@FragmentScope
public class ProjectsPresenter extends MvpNullObjectBasePresenter<ProjectsContract.View>
        implements ProjectsContract.Presenter {

    @Inject
    ProjectsPresenter() {

    }
}
