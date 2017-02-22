package co.netguru.android.inbbbox.feature.projects;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import co.netguru.android.inbbbox.data.projects.model.ui.Project;

public interface ProjectsContract {

    interface View extends MvpLceView<List<Project>> {

    }

    interface Presenter extends MvpPresenter<View> {

        void getUserProjects(long userID);
    }
}
