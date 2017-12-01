package com.loorain.live.common.http.response;

import com.loorain.live.common.http.IDontObfuscate;

/**
 * @author loorain
 * @time 2017/4/13  下午10:42
 * @desc ${TODD}
 */


public class Response<T> extends IDontObfuscate {

    public int status;

    public String msg;

    public T data;

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
