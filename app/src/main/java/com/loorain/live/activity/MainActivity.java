package com.loorain.live.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.loorain.live.R;
import com.loorain.live.fragment.LiveMainFragment;
import com.loorain.live.fragment.UserInfoFragment;

/**
 * @author loorain
 * @time 2017/6/18  下午4:14
 * @desc ${TODD}
 */


public class MainActivity extends BaseActivity {

    private final String LIVE = "live";
    private final String PUBLISH = "publish";
    private final String MY = "my";

    private Class mFragmentArray[] = {LiveMainFragment.class, Fragment.class, UserInfoFragment.class};
    private int mIconArray[] = {R.drawable.tab_live_selector, R.drawable.tab_pubish_selector, R.drawable.tab_me_selector};
    private String mTextviewArray[] = {LIVE, PUBLISH, MY};

    private FragmentTabHost mTabHost;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTabHost = obtainView(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < mFragmentArray.length; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }

    }

    @Override
    protected void initListener() {
        mTabHost.setOnTabChangedListener(tabId -> {
            if (TextUtils.equals(tabId, PUBLISH)) {
                PublishSettingActivity.invoke(MainActivity.this);
            }
        });

    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 动态获取tabicon
     *
     * @param index tab index
     * @return
     */
    private View getTabItemView(int index) {
        View view;
        if (index % 2 == 0) {
            view = LayoutInflater.from(this).inflate(R.layout.tab_live, null);
        } else {
            view = LayoutInflater.from(this).inflate(R.layout.tab_button, null);
        }
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageResource(mIconArray[index]);
        return view;
    }

}
