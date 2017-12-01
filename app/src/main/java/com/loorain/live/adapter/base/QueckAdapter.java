package com.loorain.live.adapter.base;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.ViewGroup;

import java.util.List;


/**
 * @author luzeyan
 * @time 2017/10/30 上午11:17
 * @description
 */


public class QueckAdapter<T, K extends RecyclerView.ViewHolder> extends Adapter<K> {

    protected List<T> mData;


    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public T getItem(@IntRange(from = 0L) int position) {
        return position < mData.size() ? mData.get(position) : null;
    }
}
