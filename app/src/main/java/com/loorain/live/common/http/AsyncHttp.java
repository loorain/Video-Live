package com.loorain.live.common.http;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.loorain.live.LiveApp;
import com.loorain.live.common.http.request.IRequest;
import com.loorain.live.common.http.response.Response;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author loorain
 * @time 2017/4/2  下午9:46
 * @desc ${TODD}
 */


public class AsyncHttp {

    private Gson mGson = null;

    private static AsyncHttp mInstance;

    private CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(LiveApp.sInstance));

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .build();


    private AsyncHttp() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    //将带下划线的_字段解析成不带下划线
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
        }
        OkHttpUtils.initClient(client);

    }


    public static AsyncHttp instance() {
        if (mInstance == null) {
            synchronized (AsyncHttp.class) {
                if (mInstance == null) {
                    mInstance = new AsyncHttp();
                }
            }
        }
        return mInstance;
    }

    /**
     * get请求
     *
     * @param request
     * @param listener
     */
    public void get(IRequest request, IHttpListener listener) {
        if (request != null) {
            Logger.d("post: url = ", request.getUrl());
            request.getParams().getGetBuilder().url(request.getUrl()).id(request.getRequestId()).
                    build().execute(new HttpResponse(listener,request.getParserType()));
        }
    }


    /**
     * post请求
     * @param request
     * @param listener
     */
    public void post(IRequest request, IHttpListener listener){
        if(request != null) {
            Logger.d("post: url = "+request.getUrl());
            request.getParams().getPostFormBuilder().url(request.getUrl()).id(request.getRequestId()).
                    build().execute(new HttpResponse(listener,request.getParserType()));
        } else {
            throw new RuntimeException("Request param is null");
        }
    }

    /**
     *
     * @param request
     * @param listener
     */
    public void postJson(IRequest request, IHttpListener listener){
        if(request != null) {
            if(request.getParams().has("action")) {
                Logger.d("action: "+request.getParams().getUrlParams("action"));
            }
            if(request.cleanCookie()) {
                cookieJar.getCookieStore().removeAll();
            }
            request.getParams().getPostJsonBuilder().url(request.getUrl()).id(request.getRequestId()).
                    build().execute(new HttpResponse(listener, request.getParserType()));

            Logger.d("post: url = "+request.getUrl());
        }
    }


    public void addHeader(Context context){

    }

    public void cancelRequest(String tag){
        if(tag != null) {
            OkHttpUtils.getInstance().cancelTag(tag);

        }
    }


    public interface IHttpListener {

        void onStart(int requestId);

        void onSuccess(int requestId, Response response);

        void onFailure(int requestId, int httpStatus, Throwable error);

    }

    class HttpResponse extends Callback<Response> {

        private IHttpListener mHttpListener;
        private Type mParseType;

        public HttpResponse(IHttpListener httpListener, Type parseType) {
            mHttpListener = httpListener;
            mParseType = parseType;
        }

        @Override
        public void onBefore(Request request, int id) {
            if (mHttpListener != null) {
                mHttpListener.onStart(id);
            }
        }

        @Override
        public Response parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
            Response responseData = null;
            if (mHttpListener != null && response != null) {
                if (response.isSuccessful()) {
                    String content = response.body().string();
                    if (content != null) {
                        Logger.json(content);
                        try {
                            responseData = mGson.fromJson(content, mParseType);
                        } catch (JsonSyntaxException e){
                            onError(null,e,id);
                        }
                    }
                } else {
                    onError(null,new Exception("net error"),id);
                }
            }

            return responseData;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            Logger.e(e,e.getMessage());
            if(mHttpListener != null) {
                if(!call.isCanceled()) {
                    mHttpListener.onFailure(id,0,e);
                }
            }
        }

        @Override
        public void onResponse(Response response, int id) {
            if (mHttpListener != null) {
                mHttpListener.onSuccess(id, response);
            }
        }
    }

}
