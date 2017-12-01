package com.loorain.live.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loorain.live.R;
import com.loorain.live.common.utils.OtherUtils;
import com.loorain.live.model.LiveInfo;

import java.util.ArrayList;



/**
 * @author luzeyan
 * @date 2016年7月15日
 * @description: 直播列表的Adapter
 * 列表项布局格式: R.layout.listview_video_item
 * 列表项数据格式: LiveInfo
 */
public class LiveListAdapter extends BaseQuickAdapter<LiveInfo, BaseViewHolder> {

    public LiveListAdapter(ArrayList<LiveInfo> datas) {
        super(R.layout.live_item_view, datas);
    }


    @Override
    protected void convert(BaseViewHolder holder, LiveInfo data) {
        //直播封面
        String cover = data.liveCover;
        ImageView coverIv = holder.getView(R.id.cover);
        if (TextUtils.isEmpty(cover)) {
            coverIv.setImageResource(R.drawable.bg);
        } else {
            Glide.with(mContext).load(cover).placeholder(R.drawable.bg).into(coverIv);
        }

        //主播头像
        ImageView avatar = holder.getView(R.id.avatar);
        OtherUtils.showPicWithUrl(mContext, avatar, data.userInfo.headPic, R.drawable.default_head);

        //主播昵称
        TextView hostName = holder.getView(R.id.host_name);
        if (TextUtils.isEmpty(data.userInfo.nickname)) {
            hostName.setText(OtherUtils.getLimitString(data.userId, 10));
        } else {
            hostName.setText(OtherUtils.getLimitString(data.userInfo.nickname, 10));
        }

        //主播地址
        TextView liveLbs = holder.getView(R.id.live_lbs);
        if (TextUtils.isEmpty(data.userInfo.location)) {
            liveLbs.setText(mContext.getString(R.string.live_unknown));
        } else {
            liveLbs.setText(OtherUtils.getLimitString(data.userInfo.location, 9));
        }

        //直播标题
        holder.setText(R.id.live_title, OtherUtils.getLimitString(data.title, 10));
        //直播观看人数
        holder.setText(R.id.live_members, "" + data.viewCount);
        //直播点赞数
        holder.setText(R.id.praises, "" + data.likeCount);

        //视频类型，直播或者回放
        ImageView liveLogo = holder.getView(R.id.live_logo);
        if (data.type == 0) {
            liveLogo.setImageResource(R.drawable.icon_live);
        } else {
            liveLogo.setImageResource(R.drawable.icon_video);
        }

    }
}
