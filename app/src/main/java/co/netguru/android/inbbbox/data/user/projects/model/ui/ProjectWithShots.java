package co.netguru.android.inbbbox.data.user.projects.model.ui;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.api.ProjectEntity;
import co.netguru.android.inbbbox.feature.shared.collectionadapter.ShotsCollection;

@AutoValue
public abstract class ProjectWithShots implements Parcelable, ShotsCollection {

    private static final int FIRST_NEXT_SHOT_PAGE = 2;

    public abstract long id();

    public abstract String name();

    public abstract int shotsCount();

    public abstract boolean hasMoreShots();

    public abstract int nextShotPage();

    public static ProjectWithShots create(@NonNull ProjectEntity projectEntity,
                                          @NonNull List<Shot> shotList, boolean hasMoreShots) {
        return new AutoValue_ProjectWithShots(shotList, projectEntity.id(), projectEntity.name(),
                projectEntity.shotsCount(), hasMoreShots, FIRST_NEXT_SHOT_PAGE);
    }

    public static ProjectWithShots update(ProjectWithShots project, boolean hasMoreShots) {
        return new AutoValue_ProjectWithShots(project.shots(), project.id(), project.name(),
                project.shotsCount(), hasMoreShots, project.nextShotPage() + 1);
    }

    @Override
    public long getId() {
        return id();
    }

    @Override
    public String getName() {
        return name();
    }
}
