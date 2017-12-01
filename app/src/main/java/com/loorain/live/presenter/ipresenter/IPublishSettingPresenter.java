package com.loorain.live.presenter.ipresenter;

import android.app.Activity;

import com.loorain.live.base.BasePresenter;
import com.loorain.live.base.BaseView;

import java.io.File;

/**
 * @author loorain
 * @time 2017/7/13  下午3:45
 * @desc ${TODD}
 */


public abstract class IPublishSettingPresenter implements BasePresenter {


    /**
     * 检查推流权限
     *
     * @param activity
     * @return
     */
    public abstract boolean checkPublishPermission(Activity activity);

    /**
     * 检查录制权限
     *
     * @param activity
     * @return
     */
    public abstract boolean checkScrRecorPermission(Activity activity);


    /**
     *  开始直播
     *
     * @param title
     * @param liveType
     * @param location
     * @param bitrateType
     * @param isRecord
     */
    public abstract void doPublish(String title, int liveType, String location, int bitrateType, boolean isRecord);

    /**
     * 直播定位
     */
    public abstract void doLocation();


    /**
     * 上传图片
     * @param file
     */
    public abstract void doUploadPic(File file);

    public interface IPublishSettingView extends BaseView {

        Activity getActivity();

        /**
         * 定位成功
         *
         * @param location
         */
        void doLocationSuccess(String location);

        /**
         * 定位失败
         */
        void doLocationFailed();

        /**
         * 上传成功
         *
         * @param url
         */
        void doUploadSuceess(String url);

        /**
         * 图片上传失败
         *
         */
        void doUploadFailed();

        /**
         * 结束页面
         */
        void finishActivity();
    }

}
