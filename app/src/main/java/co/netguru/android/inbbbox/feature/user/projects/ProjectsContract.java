package co.netguru.android.inbbbox.feature.user.projects;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.follower.model.ui.UserWithShots;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

public interface ProjectsContract {

    interface View extends MvpLceView<List<ProjectWithShots>>, HttpErrorView {
        void hideProgressBar();

        void addMoreProjectShots(long projectId, List<Shot> shotList);

        void showLoadingMoreShotsFromProjectView();

        void addMoreProjects(List<ProjectWithShots> projects);

        void showLoadingMoreProjectsView();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void userDataReceived(User user);

        void getUserProjects();

        void getMoreUserProjects();

        void getMoreShotsFromProject(ProjectWithShots projectWithShots);
    }
}
