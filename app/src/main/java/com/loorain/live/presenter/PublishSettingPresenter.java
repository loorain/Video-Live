package com.loorain.live.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.loorain.live.common.http.request.UploadPicRequest;
import com.loorain.live.common.logic.IUserInfoMgrListener;
import com.loorain.live.common.logic.LocationMgr;
import com.loorain.live.common.logic.UploadMgr;
import com.loorain.live.common.logic.UserInfoMgr;
import com.loorain.live.common.utils.Constants;
import com.loorain.live.presenter.ipresenter.IPublishSettingPresenter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author loorain
 * @time 2017/7/14  上午11:28
 * @desc ${TODD}
 */


public class PublishSettingPresenter extends IPublishSettingPresenter {

    IPublishSettingView mPublishLiveView;

    public static final int PICK_IMAGE_CAMERA = 100;
    public static final int PICK_IMAGE_LOCAL = 200;
    public static final int CROP_CHOOSE = 10;

    private boolean mUploading = false;

    public PublishSettingPresenter(IPublishSettingView publishLiveView) {
        mPublishLiveView = publishLiveView;
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    @Override
    public boolean checkPublishPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            String[] strings = permissions.toArray(new String[0]);
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(activity,
                        permissions.toArray(strings),
                        Constants.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkScrRecorPermission(Activity activity) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    @Override
    public void doPublish(String title, int liveType, String location, int bitrateType, boolean isRecord) {

    }


    @Override
    public void doLocation() {
        if (LocationMgr.checkLocationPermission(mPublishLiveView.getActivity())) {
            boolean success = LocationMgr.getMyLocation(mPublishLiveView.getActivity(), mOnLocationListener);
            if (!success) {
                mPublishLiveView.doLocationFailed();
            }
        }
    }


    @Override
    public void doUploadPic(File file) {
        mUploading = true;
        new UploadMgr(mPublishLiveView.getActivity(), (code, imageId, url) -> {
            if (0 == code) {
                UserInfoMgr.getInstance().setUserCoverPic(url, new IUserInfoMgrListener() {
                    @Override
                    public void onQueryUserInfo(int error, String errorMsg) {

                    }

                    @Override
                    public void onSetUserInfo(int error, String errorMsg) {

                    }
                });
            }
        }).uploadCover(UserInfoMgr.getInstance().getUserId(), file, UploadPicRequest.LIVE_COVER_TYPE);
    }

    public LocationMgr.OnLocationListener getLocationListener() {
        return mOnLocationListener;
    }


    private LocationMgr.OnLocationListener mOnLocationListener = new LocationMgr.OnLocationListener() {
        @Override
        public void onLocationChanged(int code, double lat1, double long1, String location) {
            if (0 == code) {
                mPublishLiveView.doLocationSuccess(location);
                UserInfoMgr.getInstance().setLocation(location, lat1, long1, new IUserInfoMgrListener() {
                    @Override
                    public void onQueryUserInfo(int error, String errorMsg) {
                    }

                    @Override
                    public void onSetUserInfo(int error, String errorMsg) {
                        if (0 != error) {
                            mPublishLiveView.showMsg("设置位置失败" + errorMsg);
                        }
                    }
                });
            } else {
                mPublishLiveView.doLocationFailed();
            }
        }
    };

    public Uri createCoverUri(String type) {
        String fileName = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                .format(new Date()) + ".jpg";
        String path = Environment.getExternalStorageDirectory() + "/live";

        File outputImage = new File(path, fileName);
        if (ContextCompat.checkSelfPermission(mPublishLiveView.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mPublishLiveView.getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_PERMISSION_REQ_CODE);
            return null;
        }
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (outputImage.exists()) {
                outputImage.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPublishLiveView.showMsg("生成封面失败");
        }
        return Uri.fromFile(outputImage);

    }

}
