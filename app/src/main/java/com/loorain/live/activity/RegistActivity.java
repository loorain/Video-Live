package com.loorain.live.activity;

import android.content.Context;
import android.content.Intent;

import com.loorain.live.R;


/**
 * @author loorain
 * @time 2017/5/19  下午5:49
 * @desc ${TODD}
 */


public class RegistActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    public static void invoke(Context context){
        Intent intent = new Intent(context, RegistActivity.class);
        context.startActivity(intent);
    }
}
