package com.loorain.live.fragment;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.loorain.live.R;
import com.loorain.live.activity.BaseActivity;
import com.loorain.live.presenter.LiveMainPresenter;
import com.loorain.live.ui.pagersliding.PagerSlidingTabStrip;

/**
 * @author loorain
 * @time 2017/7/3  下午1:57
 * @desc ${TODD}
 */


public class LiveMainFragment extends BaseFragment implements View.OnClickListener {

    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private ViewPager mLiveMainVp;


    private LiveMainPresenter mMainPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_main;
    }

    @Override
    protected void initView(View view) {
        pagerSlidingTabStrip = obtainView(R.id.liveMianTab);

        mLiveMainVp = obtainView(R.id.liveMainVp);
        pagerSlidingTabStrip.setTextColorResource(R.color.white);
        pagerSlidingTabStrip.setIndicatorColorResource(R.color.white);
        pagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        pagerSlidingTabStrip.setTextSelectedColorResource(R.color.white);
        pagerSlidingTabStrip.setTextSize(getResources().getDimensionPixelSize(R.dimen.h6));
        pagerSlidingTabStrip.setTextSelectedSize(getResources().getDimensionPixelSize(R.dimen.h10));
        pagerSlidingTabStrip.setUnderlineHeight(1);

    }


    @Override
    protected void initData() {

        mMainPresenter = new LiveMainPresenter((BaseActivity) getActivity());

        mLiveMainVp.setAdapter(mMainPresenter.getAdapter());
        pagerSlidingTabStrip.setViewPager(mLiveMainVp);
    }

    @Override
    protected void setListener(View view) {
        obtainView(R.id.liveMainMessageIv).setOnClickListener(this);
        obtainView(R.id.liveMainSearchIv).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liveMainSearchIv:
                break;
            case R.id.liveMainMessageIv:
                break;
        }
    }


}
