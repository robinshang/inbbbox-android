package co.netguru.android.inbbbox.data.user.projects;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.user.projects.model.api.ProjectEntity;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ProjectsApi {

    @GET("users/{userId}/projects")
    Observable<List<ProjectEntity>> getUserProjects(@Path("userId") long userId);

    @GET("projects/{projectId}/shots")
    Observable<List<ShotEntity>> getShotsFromProject(@Path("projectId") long projectId);
}
