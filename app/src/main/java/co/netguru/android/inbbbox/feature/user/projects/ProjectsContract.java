package co.netguru.android.inbbbox.feature.user.projects;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.shared.base.ErrorPresenter;
import co.netguru.android.inbbbox.feature.shared.base.HttpErrorView;

interface ProjectsContract {

    interface View extends MvpLceView<List<ProjectWithShots>>, HttpErrorView {
        void hideProgressBar();

        void addMoreProjectShots(long projectId, List<Shot> shotList, int shotsPerPage);

        void showLoadingMoreShotsFromProjectView();

        void addMoreProjects(List<ProjectWithShots> projects);

        void showLoadingMoreProjectsView();

        void showProjectDetails(ProjectWithShots projectWithShots);

        void showShotDetails(Shot shot, List<Shot> allShots);

        void showEmptyView();

        void hideEmptyView();
    }

    interface Presenter extends MvpPresenter<View>, ErrorPresenter {

        void userDataReceived(User user);

        void getUserProjects();

        void getMoreUserProjects();

        void getMoreShotsFromProject(ProjectWithShots projectWithShots);

        void onProjectClick(ProjectWithShots projectWithShots);

        void showContentForData(List<ProjectWithShots> projectWithShotsList);

        void onShotClick(Shot shot, ProjectWithShots inProject);
    }
}
