package client.tcp.com.tcpclient;

import android.app.Application;

import client.tcp.com.tcpclient.view.ActivityLifeListener;

/**
 * Created by HECP2 on 2019-7-15.
 */

public class MainApplication extends Application {
    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.registerActivityLifecycleCallbacks(new ActivityLifeListener());
    }
}
