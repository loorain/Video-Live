package com.loorain.live.presenter.ipresenter;

import com.loorain.live.base.BasePresenter;
import com.loorain.live.base.BaseView;
import com.loorain.live.model.LiveInfo;

import java.util.List;

/**
 * @author loorain
 * @time 2017/7/7  下午2:18
 * @desc ${TODD}
 */


public abstract class ILiveListPresenter implements BasePresenter {

    protected BaseView mView;

    public ILiveListPresenter(BaseView baseView){
        mView = baseView;
    }

    /**
     * 获取缓存列表
     * @return
     */
    public abstract List<LiveInfo> getLiveFormCache();

    /**
     * 重新加载列表
     * @return
     */
    public abstract boolean reloadLiveList();

    /**
     * 加载更多数据
     * @return
     */
    public abstract boolean loadDataMore();


    public interface ILiveListView extends BaseView{
        /**
         * @param retCode   获取结果，0表示成功
         * @param result    列表数据
         * @param refresh   是否需要刷新界面，首页需要刷新
         */
        void onLiveList(int retCode, final List<LiveInfo> result, boolean refresh);

        void onComplete();

    }

}
