package com.loorain.live.common.http.request;

import com.google.gson.reflect.TypeToken;
import com.loorain.live.common.http.response.Response;
import com.loorain.live.common.http.response.UploadResp;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

/**
 * @author loorain
 * @time 2017/7/21  上午10:20
 * @desc ${TODD}
 */


public class UploadPicRequest extends IRequest {

    public final static int LIVE_COVER_TYPE = 1;
    public final static int USER_HEAD_TYPE = 2;

    public UploadPicRequest(int requestId, String userId, int type, File file) throws FileNotFoundException{
        mRequestId = requestId;
        mParams.put("userId", userId);
        mParams.put("type", type);
        mParams.put("file", file);
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<UploadResp>>(){}.getType();
    }

    @Override
    public String getUrl() {
        return getHost() + "Image/upload";
    }
}
