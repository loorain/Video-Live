package com.loorain.live.common.http.request;

import com.google.gson.reflect.TypeToken;
import com.loorain.live.common.http.response.Response;
import com.loorain.live.model.UserInfo;
import com.loorain.live.common.utils.CipherUtil;

import java.lang.reflect.Type;

/**
 * @author loorain
 * @time 2017/4/13  下午11:49
 * @desc ${TODD}
 */


public class LoginRequest extends IRequest {

    public static final int NORMAL_LONGIN = 0;
    public static final int CNIAO_LOGIN = 1;

    public LoginRequest(int requestId, String userName, String password, int loginType) {
        mRequestId = requestId;
        if (loginType == NORMAL_LONGIN) {
            mParams.put("action", "login");

        } else if (loginType == CNIAO_LOGIN) {
            mParams.put("action", "loginCniaow");
        }
        mParams.put("userName", userName);
        if (mParams.getUrlParams("action").equals("loginCniaow")) {
            mParams.put("password", CipherUtil.getAESInfo(password));
        } else {
            mParams.put("password", password);
        }
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<UserInfo>>() {
        }.getType();
    }

    @Override
    public String getUrl() {
        return getHost() + "User";
    }
}
