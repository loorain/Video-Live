package com.loorain.live;

import android.app.Application;

/**
 * @author loorain
 * @time 2017/4/9  下午4:00
 * @desc ${TODD}
 */


public class LiveApp extends Application {

    private static final String BUGLY_APPID = "1400012894";

    private static LiveApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initSDK();


    }

    public static LiveApp getApplication() {
        return instance;
    }

    private void initSDK() {

    }
}
