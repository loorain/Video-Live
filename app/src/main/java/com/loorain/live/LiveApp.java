package com.loorain.live;

import android.app.Application;

import com.loorain.live.common.logic.IMInitMgr;

/**
 * @author loorain
 * @time 2017/4/9  下午4:00
 * @desc ${TODD}
 */


public class LiveApp extends Application {

    private static final String BUGLY_APPID = "1400012894";

    public static LiveApp sInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        initSDK();

    }




    private void initSDK() {
        IMInitMgr.init(this);


    }
}
