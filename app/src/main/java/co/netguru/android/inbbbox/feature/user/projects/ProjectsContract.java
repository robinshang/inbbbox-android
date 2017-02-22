package co.netguru.android.inbbbox.feature.user.projects;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;

public interface ProjectsContract {

    interface View extends MvpLceView<List<ProjectWithShots>> {
        void hideProgressBar();
    }

    interface Presenter extends MvpPresenter<View> {

        void userDataReceived(UserWithShots user);

        void getUserProjects();
    }
}
