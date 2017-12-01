package com.loorain.live.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 * @author loorain
 * @time 2017/4/2  下午8:20
 * @desc ${TODD}
 */


public abstract class BaseActivity extends FragmentActivity {

    protected Context mContext;
    protected Handler mHandler = new Handler();

    public ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setBeforeLayout();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        initView();
        initListener();
        initData();
    }


    protected void setBeforeLayout() {}

    /**
     * 返回当前界面布局文件
     */
    protected abstract int getLayoutId();

    /**
     * 此方法描述的是： 初始化所有view
     */
    protected abstract void initView();

    /**
     * 此方法描述的是： 初始化所有数据的方法
     */
    protected abstract void initListener();

    /**
     * 此方法描述的是： 设置所有事件监听
     */
    protected abstract void initData();



    public <T extends View> T obtainView(int resId){
        return (T) findViewById(resId);
    }

    /**
     * 显示toast
     *
     * @param resId
     */
    public void showToast(final int resId) {
        showToast(getString(resId));
    }

    /**
     * 显示toast
     *
     * @param resStr
     * @return Toast对象，便于控制toast的显示与关闭
     */
    public void showToast(final String resStr) {

        if (TextUtils.isEmpty(resStr)) {
            return ;
        }
        mHandler.post(() -> {
            Toast toast = Toast.makeText(BaseActivity.this, resStr,
                    Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    public void showDialog(){

    }

    public void dismissDialog(){

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}


