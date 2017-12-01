package com.loorain.live.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.loorain.live.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseActivity {

    private static final int START_LOGIN = 2873;
    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Message msg = Message.obtain();
        msg.arg1 = START_LOGIN;
        mHandler.sendMessageDelayed(msg, 2000);

    }

    @Override
    public void onBackPressed() {

    }

    private static class MyHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;

        private MyHandler(SplashActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivity.get();
            LoginActivity.invoke(activity);
            activity.finish();
//            if (activity != null) {
//                if (null != UserInfoCache.getaToken(LiveApp.getApplication())) {
//                    MainActivity.invoke(activity);
//                } else {
//                    LoginActivity.invoke(activity);
//                }
//                activity.finish();
//            }
        }
    }

}
