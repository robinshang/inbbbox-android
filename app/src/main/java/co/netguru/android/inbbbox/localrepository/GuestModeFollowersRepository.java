package co.netguru.android.inbbbox.localrepository;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class GuestModeFollowersRepository {

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public GuestModeFollowersRepository(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }
}
