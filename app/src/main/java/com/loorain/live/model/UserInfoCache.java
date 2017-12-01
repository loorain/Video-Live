package com.loorain.live.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.loorain.live.common.http.IDontObfuscate;
import com.loorain.live.common.utils.AsimpleCache.ACache;
import com.loorain.live.common.utils.Constants;


/**
 * @description: 存储用户信息
 * @author: loorain
 * @time: 2016/10/31 18:07
 */
public class UserInfoCache extends IDontObfuscate {

    public static void saveCache(Context context, UserInfo info) {
        Log.i(UserInfoCache.class.getSimpleName(), "saveCache: info.headPicSmall = " + info.headPicSmall);
        ACache.get(context).put(Constants.USER_ID, info.userId);
        ACache.get(context).put(Constants.USER_NICK_NAME, info.nickname);
        ACache.get(context).put(Constants.USER_HEADPIC, info.headPic);
        ACache.get(context).put(Constants.USER_HEADPIC_SMALL, info.headPicSmall);
        ACache.get(context).put(Constants.USER_SIG_ID, info.sigId);
        ACache.get(context).put(Constants.TOKEN, info.token);
        ACache.get(context).put(Constants.SDK_APP_ID, info.sdkAppId);
        ACache.get(context).put(Constants.ADK_ACCOUNT_TYPE, info.sdkAccountType);

        if (info.sdkAppId != null && TextUtils.isDigitsOnly(info.sdkAppId)) {
            Constants.IMSDK_APPID = Integer.parseInt(info.sdkAppId);
        }
        if (info.sdkAccountType != null && TextUtils.isDigitsOnly(info.sdkAccountType)) {
            Constants.IMSDK_ACCOUNT_TYPE = Integer.parseInt(info.sdkAccountType);
        }
    }

    public static String getUserId(Context context) {
        return ACache.get(context).getAsString(Constants.USER_ID);
    }

    public static String getNickname(Context context) {
        return ACache.get(context).getAsString(Constants.USER_NICK_NAME);
    }

    public static String getHeadPic(Context context) {
        return ACache.get(context).getAsString(Constants.USER_HEADPIC);
    }

    public static String getSigId(Context context) {
        return ACache.get(context).getAsString(Constants.USER_SIG_ID);
    }

    public static String getaToken(Context context) {
        return ACache.get(context).getAsString(Constants.TOKEN);
    }

    public static String getSdkAppId(Context context) {
        return ACache.get(context).getAsString(Constants.SDK_APP_ID);
    }

    public static String getAccountType(Context context) {
        return ACache.get(context).getAsString(Constants.ADK_ACCOUNT_TYPE);
    }


    public static void clearCache(Context context) {
        ACache.get(context).remove(Constants.USER_ID);
        ACache.get(context).remove(Constants.USER_NICK_NAME);
        ACache.get(context).remove(Constants.USER_HEADPIC);
        ACache.get(context).remove(Constants.USER_SIG_ID);
        ACache.get(context).remove(Constants.TOKEN);
        ACache.get(context).remove(Constants.SDK_APP_ID);
        ACache.get(context).remove(Constants.ADK_ACCOUNT_TYPE);
    }
}
