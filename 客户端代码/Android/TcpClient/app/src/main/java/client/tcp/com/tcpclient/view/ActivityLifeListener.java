package client.tcp.com.tcpclient.view;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import client.tcp.com.tcpclient.cache.Cache;

/**
 * Created by HECP2 on 2019-7-16.
 */

public class ActivityLifeListener implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        Cache.getInstance().activityNum++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Cache.getInstance().activityNum--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
