package co.netguru.android.inbbbox.data.user.projects.model.ui;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import co.netguru.android.inbbbox.data.user.projects.model.api.ProjectEntity;

@AutoValue
public abstract class Project {

    public abstract long id();

    public abstract String name();

    public abstract int shotsCount();

    public static Project create(@NonNull ProjectEntity projectEntity) {
        return new AutoValue_Project(projectEntity.id(), projectEntity.name(),
                projectEntity.shotsCount());
    }
}
