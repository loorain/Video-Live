package com.loorain.live.common.http.request;

import com.google.gson.reflect.TypeToken;
import com.loorain.live.common.http.response.ResList;
import com.loorain.live.common.http.response.Response;
import com.loorain.live.model.LiveInfo;

import java.lang.reflect.Type;

/**
 * @author loorain
 * @time 2017/7/11  上午11:26
 * @desc ${TODD}
 */


public class LiveListRequest extends IRequest{

    public LiveListRequest(int requestId, String userId, int pageIndex, int pageSize) {
        mRequestId = requestId;
        mParams.put("action","liveList");
        mParams.put("userId", userId);
        mParams.put("pageIndex", pageIndex);
        mParams.put("pageSize", pageSize);
    }

    @Override
    public Type getParserType() {
        return new TypeToken<Response<ResList<LiveInfo>>>() {}.getType();
    }

    @Override
    public String getUrl() {
        return getHost() + "Live";
    }
}
