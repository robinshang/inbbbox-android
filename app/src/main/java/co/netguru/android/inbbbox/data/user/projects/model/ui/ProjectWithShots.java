package co.netguru.android.inbbbox.data.user.projects.model.ui;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.api.ProjectEntity;

@AutoValue
public abstract class ProjectWithShots {

    public abstract long id();

    public abstract String name();

    public abstract int shotsCount();

    public abstract List<Shot> shotList();

    public static ProjectWithShots create(@NonNull ProjectEntity projectEntity, @NonNull List<Shot> shotList) {
        return new AutoValue_ProjectWithShots(projectEntity.id(), projectEntity.name(),
                projectEntity.shotsCount(), shotList);
    }
}
