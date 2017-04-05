package com.loorain.live.base;

import android.content.Context;

/**
 * @author loorain
 * @time 2017/4/2  下午8:57
 * @desc ${TODD}
 */


public interface BaseView<T> {

    /**
     * 显示加载框
     */
    void showLoading();

    /**
     * 隐藏加载框
     */
    void dismissLoading();

    /**
     * 显示信息
     * @param msg
     */
    void showMsg(String msg);

    void showMsg(int resId);

    /**
     * 获取上下文
     * @return
     */
    Context getContext();
}
