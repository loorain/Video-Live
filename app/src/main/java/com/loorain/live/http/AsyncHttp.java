package com.loorain.live.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author loorain
 * @time 2017/4/2  下午9:46
 * @desc ${TODD}
 */


public class AsyncHttp {

    private AsyncHttp(){}

    private static AsyncHttp mInstance;

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS).build();


    public static AsyncHttp instance(){
        if(mInstance == null) {
           synchronized (AsyncHttp.class){
               if(mInstance == null) {
                   mInstance = new AsyncHttp();
               }
           }
        }
        return mInstance;
    }
}
