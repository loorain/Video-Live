package com.loorain.live.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loorain.live.R;
import com.loorain.live.common.logic.LocationMgr;
import com.loorain.live.common.utils.Constants;
import com.loorain.live.common.utils.ToastUtils;
import com.loorain.live.presenter.PublishSettingPresenter;
import com.loorain.live.presenter.ipresenter.IPublishSettingPresenter;
import com.loorain.live.ui.TakePhotoDialog;
import com.loorain.live.ui.TakePhotoPop;
import com.loorain.live.ui.customviews.CustomSwitch;

import java.io.File;

import static com.loorain.live.R.id.btn_lbs;
import static com.loorain.live.R.id.btn_record;

/**
 * @author loorain
 * @time 2017/7/12  下午2:10
 * @desc ${TODD}
 */


public class PublishSettingActivity extends BaseActivity implements View.OnClickListener, IPublishSettingPresenter.IPublishSettingView {

    private Dialog mPicChsDialog;
    private ImageView ivCover;
    private TextView tvPicTip;
    private TextView tvLBS;
    private TextView tvRecord;
    private CustomSwitch btnLBS;
    private CustomSwitch btnRecord;
    private RadioGroup mRGBitrate;
    private RadioGroup mRGRecordType;

    private Uri fileUri, cropUri;

    private boolean mPermission = false;
    private PublishSettingPresenter mPresenter;
    private TakePhotoDialog mPhotoDialog;
    private TakePhotoPop mTakePhotoPop;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_setting;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void initView() {
        tvPicTip = obtainView(R.id.tv_pic_tip);
        ivCover = obtainView(R.id.cover);
        tvLBS = obtainView(R.id.address);
        tvRecord = obtainView(R.id.tv_record);
        btnLBS = obtainView(btn_lbs);
        btnRecord = obtainView(btn_record);
        mRGRecordType = obtainView(R.id.rg_record_type);
        mRGBitrate = obtainView(R.id.rg_bitrate);

    }

    @Override
    protected void initListener() {
        obtainView(R.id.btn_cancel).setOnClickListener(this);
        obtainView(R.id.btn_publish).setOnClickListener(this);

        btnLBS.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPresenter = new PublishSettingPresenter(this);
        mPermission = mPresenter.checkPublishPermission(this);

    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, PublishSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lbs:
                if (btnLBS.getChecked()) {
                    btnLBS.setChecked(false, true);
                    tvLBS.setText(R.string.text_live_close_lbs);
                } else {
                    btnLBS.setChecked(true, true);
                    tvLBS.setText(R.string.text_live_location);
                    mPresenter.doLocation();
                }
                break;
            case R.id.btn_record:
                if (btnRecord.getChecked()) {
                    btnRecord.setChecked(false, true);
                    tvRecord.setText(R.string.text_live_record_no);
                } else {
                    btnRecord.setChecked(true, true);
                    tvRecord.setText(R.string.text_live_record_yes);
                }
                break;
            case R.id.cover:
                takePhoto();

                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_publish:
                // TODO: 2017/11/2 发起直播
                break;
            default:
        }
    }

    private void takePhoto() {
        mPhotoDialog = new TakePhotoDialog.Builder(this).setListener(new TakePhotoDialog.OnBitmapReturn() {
            @Override
            public void returnBitmap(Bitmap bitmap, File file) {
                mPresenter.doUploadPic(file);

            }

            @Override
            public void captureFailed(String msg) {
                ToastUtils.show(msg);
            }
        }).build();

        mPhotoDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPhotoDialog.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.LOCATION_PERMISSION_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (!LocationMgr.getMyLocation(this, mPresenter.getLocationListener())) {
                        tvLBS.setText(R.string.text_live_lbs_fail);
                        btnLBS.setChecked(false, false);
                    }
                }
                break;
            case Constants.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mPermission = true;
                break;
            default:
                break;
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showMsg(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void showMsg(int resId) {
        //        ToastUtils.show(resId);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void doLocationSuccess(String location) {
        tvLBS.setText(location);
    }

    @Override
    public void doLocationFailed() {
        tvLBS.setText(R.string.text_live_lbs_fail);
        btnLBS.setChecked(false, false);
    }

    @Override
    public void doUploadSuceess(String url) {
        Glide.with(this).load(url).into(ivCover);
    }

    @Override
    public void doUploadFailed() {
        showMsg(R.string.live_cover_upload_failed);
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
