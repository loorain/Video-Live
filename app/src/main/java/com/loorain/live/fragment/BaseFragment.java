package com.loorain.live.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author loorain
 * @time 2017/4/2  下午8:34
 * @desc ${TODD}
 */


public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected Handler mHandler = new Handler();
    protected View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (Activity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getLayoutId() != 0) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        } else {
            try {
                throw new Exception("layout is empty");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initView(rootView);
        initData();
        setListener(rootView);
        return rootView;
    }
    /**
     * 返回当前界面布局文件
     */
    protected abstract int getLayoutId();

    /**
     * 此方法描述的是： 初始化所有view
     */
    protected abstract void initView(View view);

    /**
     * 此方法描述的是： 初始化所有数据的方法
     */
    protected abstract void initData();

    /**
     * 此方法描述的是： 设置所有事件监听
     */
    protected abstract void setListener(View view);

    /**
     * 显示toast
     *
     * @param resId
     */
    public void showToast(final int resId) {
        showToast(getString(resId));
    }


    public <T extends View> T obtainView(int resId) {
        return (T) rootView.findViewById(resId);
    }

    /**
     * 显示toast
     *
     * @param resStr
     * @return Toast对象，便于控制toast的显示与关闭
     */
    public Toast showToast(final String resStr) {

        if (TextUtils.isEmpty(resStr)) {
            return null;
        }

        Toast toast = null;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(mContext, resStr,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        return toast;
    }
}
