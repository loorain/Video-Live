package com.loorain.live.presenter;

import com.loorain.live.common.http.AsyncHttp;
import com.loorain.live.common.http.request.LiveListRequest;
import com.loorain.live.common.http.request.RequestComm;
import com.loorain.live.common.http.response.ResList;
import com.loorain.live.common.http.response.Response;
import com.loorain.live.common.logic.UserInfoMgr;
import com.loorain.live.model.LiveInfo;
import com.loorain.live.presenter.ipresenter.ILiveListPresenter;
import com.loorain.live.common.utils.Constants;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loorain
 * @time 2017/7/7  下午2:17
 * @desc ${TODD}
 */


public class LiveListPresenter extends ILiveListPresenter {


    private boolean mHasMore;
    private boolean isLoading;
    private ArrayList<LiveInfo> mLiveInfoList = new ArrayList<>();
    private ILiveListView mListView;

    public LiveListPresenter(ILiveListView listView) {
        super(listView);
        mListView = listView;
    }

    @Override
    public ArrayList<LiveInfo> getLiveFormCache() {
        return mLiveInfoList;
    }

    @Override
    public boolean reloadLiveList() {
        mLiveInfoList.clear();
        fetchLiveList(RequestComm.live_list, UserInfoMgr.getInstance().getUserId(), 1, Constants.PAGESIZE);
        return true;
    }

    @Override
    public boolean loadDataMore() {
        if (mHasMore) {
            int pageIndex = mLiveInfoList.size() / Constants.PAGESIZE + 1;
            fetchLiveList(RequestComm.live_list_more, UserInfoMgr.getInstance().getUserId(), pageIndex, Constants.PAGESIZE);
        }

        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isHasMore() {
        return mHasMore;
    }

    /**
     * 获取直播列表
     *
     * @param type      1:拉取在线直播列表 2:拉取7天内录播列表 3:拉取在线直播和7天内录播列表，直播列表在前，录播列表在后
     * @param pageIndex 页数
     * @param pageSize  每页个数
     */
    public void fetchLiveList(int type, String userId, final int pageIndex, int pageSize) {
        LiveListRequest request = new LiveListRequest(type, userId, pageIndex, pageSize);
        AsyncHttp.instance().postJson(request, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {
                isLoading = true;
            }

            @Override
            public void onSuccess(int requestId, Response response) {
                isLoading = false;
                mListView.onComplete();
                if (response.status == RequestComm.SUCCESS) {
                    ResList<LiveInfo> resList = (ResList<LiveInfo>) response.data;
                    if (resList != null) {
                        Logger.d(resList);
                        List<LiveInfo> result = resList.items;
                        if (result != null) {
                            //判断是否有下一页
                            if (!result.isEmpty()) {
                                mLiveInfoList.addAll(result);
                                mHasMore = (mLiveInfoList.size() >= pageIndex * Constants.PAGESIZE);
                            } else {
                                mHasMore = false;
                            }

                            //
                            if (mListView != null) {
                                mListView.onLiveList(0, result, pageIndex == 1);
                            }
                        } else {
                            if (mListView != null) {
                                mListView.onLiveList(0, result, pageIndex == 1);
                            }
                        }
                    } else {
                        if (mListView != null) {
                            mListView.onLiveList(1, null, true);
                        }
                    }
                } else {
                    if (mListView != null) {
                        mListView.onLiveList(1, null, true);
                    }
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {
                isLoading = false;
                mListView.onComplete();
                if (mListView != null) {
                    mListView.onLiveList(1, null, false);
                }
            }
        });
    }

}
