package com.loorain.live.http.request;

import com.google.gson.Gson;
import com.loorain.live.LiveApp;
import com.loorain.live.http.IDontObfuscate;
import com.loorain.live.model.UserInfoCache;

import java.lang.reflect.Type;

/**
 * @author loorain
 * @time 2017/4/3  下午1:20
 * @desc 网络请求基本类
 */


public abstract class IRequest extends IDontObfuscate {

    public boolean DEBUG = false;

    public static final String HOST_DEBUG = "http://192.168.31.92:8094/Api/";
    public static final String HOST_PUBLIC = "http://live.demo.cniao5.com/Api/";

    protected RequestParams mParams = new RequestParams();
    public int mRequestId = 0;
    protected int mDraw = 0;
    protected final static Gson mGson = new Gson();

    public IRequest() {
    }

    public RequestParams getParams() {
        String token = UserInfoCache.getaToken(LiveApp.getApplication());
        if (token != null) {
            mParams.put("token", token);
        }
        return mParams;
    }

    /**
     * @return  当前解析类型
     */
    public abstract Type getParserType();

    /**
     * @return 当前url地址
     */
    public abstract String getUrl();

    /**
     * @return  返回请求接口唯一标识
     */
    public int getRequestId() {
        return mRequestId;
    }

    public void setRequestId(int requestId) {
        mRequestId = requestId;
    }

    public String getHost() {
        return DEBUG ? HOST_DEBUG : HOST_PUBLIC;
    }

    public boolean isCache() {
        return false;
    }

    public boolean cleanCookie() {
        return false;
    }

}
