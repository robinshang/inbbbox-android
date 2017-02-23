package co.netguru.android.inbbbox.feature.user.projects;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

public interface ProjectsContract {

    interface View extends MvpLceView<List<ProjectWithShots>>, HttpErrorView {
        void hideProgressBar();

        void addMoreProjectShots(long projectId, List<Shot> shotList);
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void userDataReceived(UserWithShots user);

        void getUserProjects();

        void getMoreShotsFromProject(ProjectWithShots projectWithShots);
    }
}
