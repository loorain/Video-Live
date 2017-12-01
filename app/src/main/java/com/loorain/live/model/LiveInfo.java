package com.loorain.live.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @description: 直播信息
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public class LiveInfo implements Serializable{

    @SerializedName("user_id")
    public String userId = "123";
    public String groupId = "123";
    public String liveId = "123";
    public long createTime = System.currentTimeMillis();
    public int type = 1;
    public int viewCount = 2000;
    public int likeCount = 1000;
    public String title = "游戏主播";
    public String playUrl = "";
    public String fileId;
    public String liveCover = "http://dynamic-image.yesky.com/220x165/uploadImages/2016/233/19/78L91PEK4KND.jpg";

    //TCLiveUserInfo
    public TCLiveUserInfo userInfo = new TCLiveUserInfo();


    public class TCLiveUserInfo implements Serializable {
        public String userId;
        public String nickname = "nickname";
        public String headPic = "http://img.jsqq.net/uploads/allimg/141105/1_141105191950_13.jpg";
        public String location;

    }
}
