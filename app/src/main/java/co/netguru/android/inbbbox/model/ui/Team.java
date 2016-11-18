package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import co.netguru.android.inbbbox.model.api.TeamEntity;

@AutoValue
public abstract class Team implements Parcelable {

    public abstract long id();

    public abstract String name();

    public static Team.Builder builder() {
        return new AutoValue_Team.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Team.Builder id(long id);

        public abstract Team.Builder name(String name);

        public abstract Team build();
    }

    public static Team create(TeamEntity entity) {
        return Team.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
