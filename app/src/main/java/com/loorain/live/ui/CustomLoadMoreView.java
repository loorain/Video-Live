package com.loorain.live.ui;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.loorain.live.R;

/**
 * @author luzeyan
 * @time 2017/10/30 下午1:56
 * @description
 */


public class CustomLoadMoreView extends LoadMoreView{
    @Override
    public int getLayoutId() {
        return R.layout.layout_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
