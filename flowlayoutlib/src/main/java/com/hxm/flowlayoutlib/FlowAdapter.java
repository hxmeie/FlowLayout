package com.hxm.flowlayoutlib;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by hxm on 2018/2/24.
 * 描述：
 */
public abstract class FlowAdapter implements ItemSelectListener{

    OnItemClickListener onItemClickListener;

    public abstract View getView(FlowLayout parent, LayoutInflater inflater, int position);

    /**
     * 获取标签数量
     *
     * @return int
     */
    public abstract int getCount();

    /**
     * 设置标签的点击事件监听
     *
     * @param onItemClickListener OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemSelect(View view, int position) {

    }

    @Override
    public void onItemUnSelect(View view, int position) {

    }

    @Override
    public void canNotSelect(View view, int position) {

    }

    @Override
    public void onExceedsLimit(int limit) {

    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
