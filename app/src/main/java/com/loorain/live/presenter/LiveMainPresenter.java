package com.loorain.live.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.loorain.live.activity.BaseActivity;
import com.loorain.live.base.BasePresenter;
import com.loorain.live.fragment.LiveListFragment;


/**
 * @author loorain
 * @time 2017/7/3  下午11:27
 * @desc ${TODD}
 */


public class LiveMainPresenter implements BasePresenter {

    private static final int TYPE_LIST = 0;
    private static final int TYPE_DOYEN = 1;

    private BaseActivity mContext;

    public final String[] TITLES = new String[]{"最新", "最热", "达人", "美女"};
    public static final int[] TYPES = new int[]{TYPE_LIST, TYPE_LIST, TYPE_DOYEN, TYPE_LIST};



    public LiveMainPresenter(BaseActivity activity) {
        mContext = activity;
//        mTitles = activity.getResources().getStringArray(R.array.liveMianTabs);
//        mTypes = activity.getResources().getIntArray(R.array.liveMainTpye);
    }

    public LiveMainPresenter() {

    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    public FragmentStatePagerAdapter getAdapter() {
        return new LiveMainPresenter.FragmentAdapter(mContext.getSupportFragmentManager());
    }

    class FragmentAdapter extends FragmentStatePagerAdapter{


        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(TYPES[position] == TYPE_LIST) {
                return LiveListFragment.newInstance(position);
            } else {
                return LiveListFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }


}
