package com.loorain.live.http.response;

import com.loorain.live.http.IDontObfuscate;

/**
 * @author loorain
 * @time 2017/4/13  下午10:42
 * @desc ${TODD}
 */


public class Response<T> extends IDontObfuscate {

    private int status;

    private String msg;

    private T data;

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
