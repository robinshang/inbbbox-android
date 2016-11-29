package co.netguru.android.inbbbox.model.ui;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Person implements Parcelable {

    public abstract String name();

    public abstract String avatarUrl();

    @Nullable
    public abstract List<Shot> shotList();

    public static Person.Builder builder(){
        return new AutoValue_Person.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Person.Builder name(String name);

        public abstract Person.Builder avatarUrl(String url);

        public abstract Person.Builder shotList(List<Shot> shotList);

        public abstract Person build();
    }

    public static Person create(String name, String avatarUrl, List<Shot> shotList){
        return Person.builder()
                .name(name)
                .avatarUrl(avatarUrl)
                .shotList(shotList)
                .build();
    }
}