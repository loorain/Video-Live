package com.loorain.live.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * @author luzeyan
 * @time 2017/10/30 下午2:16
 * @description
 */


public class CustomProgressBar extends ProgressBar{

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

    }


}
