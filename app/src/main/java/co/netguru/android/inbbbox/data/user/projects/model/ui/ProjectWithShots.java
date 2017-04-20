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

    @Override
    public long getId() {
        return id();
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public ShotsCollection updatePageStatus(boolean hasMoreShots) {
        return update(this, hasMoreShots);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract ProjectWithShots.Builder id(long id);

        public abstract ProjectWithShots.Builder name(String name);

        public abstract ProjectWithShots.Builder shotsCount(int shotsCount);

        public abstract ProjectWithShots.Builder shots(List<Shot> shots);

        public abstract ProjectWithShots.Builder hasMoreShots(boolean hasMoreShots);

        public abstract ProjectWithShots.Builder nextShotPage(int nextShotPage);

        public abstract ProjectWithShots build();
    }

    public static ProjectWithShots create(@NonNull ProjectEntity projectEntity,
                                          @NonNull List<Shot> shotList, boolean hasMoreShots) {
        return ProjectWithShots.builder()
                .id(projectEntity.id())
                .name(projectEntity.name())
                .shotsCount(projectEntity.shotsCount())
                .shots(shotList)
                .hasMoreShots(hasMoreShots)
                .nextShotPage(FIRST_NEXT_SHOT_PAGE)
                .build();
    }

    public static ProjectWithShots update(ProjectWithShots project, boolean hasMoreShots) {
        return ProjectWithShots.builder()
                .id(project.id())
                .name(project.name())
                .shotsCount(project.shotsCount())
                .shots(project.shots())
                .hasMoreShots(hasMoreShots)
                .nextShotPage(project.nextShotPage() + 1)
                .build();
    }


    public static ProjectWithShots.Builder builder() {
        return new AutoValue_ProjectWithShots.Builder();
    }

}
