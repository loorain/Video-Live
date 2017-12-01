package com.loorain.live.fragment;

import android.os.Bundle;
import android.view.View;

import com.loorain.live.R;

/**
 * @author loorain
 * @time 2017/7/3  下午1:57
 * @desc ${TODD}
 */


public class LivePupolarUserFragment extends BaseFragment {

    public static LivePupolarUserFragment newInstance(int position){
        LivePupolarUserFragment fragment = new LivePupolarUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_list;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener(View view) {

    }
}
