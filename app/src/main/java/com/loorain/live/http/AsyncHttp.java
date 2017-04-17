package com.loorain.live.http;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loorain.live.LiveApp;
import com.loorain.live.http.request.IRequest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * @author loorain
 * @time 2017/4/2  下午9:46
 * @desc ${TODD}
 */


public class AsyncHttp {

    private Gson mGson = null;

    private static AsyncHttp mInstance;

    private CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(LiveApp.getApplication()));

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .build();


    private AsyncHttp(){
        if(mGson == null) {
            mGson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
        }
        OkHttpUtils.initClient(client);

    }


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

    public void get(IRequest request,IHttpListener listener){


    }




    public interface IHttpListener{

        void onStart(int requestId);

        void onSuccess(int requestId, Response response);

        void onFailure(int requestId, Response response);

    }


}
