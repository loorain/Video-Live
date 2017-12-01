package com.loorain.live.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.loorain.live.R;
import com.loorain.live.adapter.LiveListAdapter;
import com.loorain.live.common.utils.ToastUtils;
import com.loorain.live.model.LiveInfo;
import com.loorain.live.presenter.LiveListPresenter;
import com.loorain.live.presenter.ipresenter.ILiveListPresenter;
import com.loorain.live.ui.CustomLoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loorain
 * @time 2017/7/3  下午1:57
 * @desc ${TODD}
 */


public class LiveListFragment extends BaseFragment implements ILiveListPresenter.ILiveListView,
        BaseQuickAdapter.RequestLoadMoreListener {


    private RecyclerView mLiveRecycle;
    private SmartRefreshLayout mLiveRefresh;
    private LiveListPresenter mLIstPresenter;
    private LiveListAdapter mListAdapter;

    public static LiveListFragment newInstance(int position) {
        LiveListFragment fragment = new LiveListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_list;
    }

    @Override
    protected void initView(View view) {

        mLiveRecycle = obtainView(R.id.liveRecycle);
        mLiveRefresh = obtainView(R.id.live_refresh);
        mLiveRefresh.setRefreshHeader(new ClassicsHeader(mContext));
        mLIstPresenter = new LiveListPresenter(this);

        mListAdapter = new LiveListAdapter(
                (ArrayList<LiveInfo>) mLIstPresenter.getLiveFormCache().clone());
        mListAdapter.setEnableLoadMore(true);
        mListAdapter.disableLoadMoreIfNotFullPage(mLiveRecycle);
        mListAdapter.setOnLoadMoreListener(this, mLiveRecycle);
        mListAdapter.setLoadMoreView(new CustomLoadMoreView());

        mLiveRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        mLiveRecycle.setAdapter(mListAdapter);
    }

    @Override
    protected void initData() {
        refreshRecyclerView();
    }

    @Override
    protected void setListener(View view) {
        mLiveRefresh.setOnRefreshListener(refreshlayout -> refreshRecyclerView());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void onComplete() {
        mLiveRefresh.finishRefresh();
        mListAdapter.loadMoreComplete();
    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void showMsg(int resId) {
        ToastUtils.show(resId);
    }

    @Override
    public void onLiveList(int retCode, List<LiveInfo> result, boolean refresh) {
        if (mListAdapter.getItemCount() >= 30) {
            mListAdapter.setEnableLoadMore(false);
        }
        if (refresh) {
            mListAdapter.setNewData(result);
        } else {
            mListAdapter.addData(result);
        }
        onComplete();
    }


    @Override
    public void onLoadMoreRequested() {
        new Handler().postDelayed(() -> mLIstPresenter.loadDataMore(), 1000);
    }


    private void refreshRecyclerView() {
        if (mLIstPresenter.reloadLiveList()) {
            mLiveRefresh.post(() -> {
                //                    mLiveRefresh.setRefreshing(true);
            });
        }
    }

}
