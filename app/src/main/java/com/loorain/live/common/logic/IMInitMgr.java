package com.loorain.live.common.logic;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.loorain.live.common.utils.Constants;
import com.tencent.TIMGroupSettings;
import com.tencent.TIMManager;
import com.tencent.TIMUserStatusListener;

/**
 * Author: luzeyan
 * Time: 2017/8/24 下午4:40
 * Description: IM 初始化管理
 */


public class IMInitMgr {

    //标志确定 SDK 是否初始化，避免客户 SDK 未初始化的情况，实现可重入的 init 操作
    private static boolean isSDKInit = false;

    public static void init(Context context) {
        if(isSDKInit) {
            return;
        }
        Log.i("TAG", "IMInitMgr init()" + context);
        //设置通讯管理器
        TIMManager instance = TIMManager.getInstance();
        //禁止服务器自动代替上报已读
        instance.disableAutoReport();
        //初始化 imsdk
        instance.init(context);
        //初始化群设置
        instance.initGroupSettings(new TIMGroupSettings());
        //注册 sig 失效监听回调
        instance.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.EXIT_APP));
            }

            @Override
            public void onUserSigExpired() {
                IMLogin.getInstance().reLogin();
            }
        });

        //初始化登录模块
        IMLogin.getInstance().init(context);

        isSDKInit = true;
    }
}
