package com.loorain.live.common.http.request;

import com.google.gson.reflect.TypeToken;
import com.loorain.live.common.http.response.Response;

import java.lang.reflect.Type;

/**
 * @author loorain
 * @time 2017/5/20  下午12:41
 * @desc ${TODD}
 */


public class PhoneLoginRequest extends IRequest {

    public PhoneLoginRequest(int requestId, String mobile, String verifyCode) {
        mRequestId = requestId;
        mParams.put("action","phoneLogin");
        mParams.put("mobile",mobile);
        mParams.put("verifyCode",verifyCode);
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response>(){
        }.getType();
    }

    @Override
    public String getUrl() {
        return getHost()+"User";
    }
}
