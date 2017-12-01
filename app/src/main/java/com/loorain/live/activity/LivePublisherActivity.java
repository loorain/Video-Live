package com.loorain.live.activity;

import com.loorain.live.R;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * @author luzeyan
 * @time 2017/10/27 下午10:33
 * @description
 */


public class LivePublisherActivity extends BaseActivity{


    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_live;
    }

    @Override
    protected void initView() {
        TXCloudVideoView videoView = obtainView(R.id.video_view);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
