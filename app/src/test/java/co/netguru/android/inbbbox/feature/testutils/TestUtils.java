package co.netguru.android.inbbbox.feature.testutils;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

public class TestUtils {
    protected void setupJavaThreadingManagementForTests(){
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        RxJavaHooks.setOnIOScheduler(scheduler -> Schedulers.immediate());
    }

    protected void resetJavaThreading(){
        RxAndroidPlugins.getInstance().reset();
    }
}
