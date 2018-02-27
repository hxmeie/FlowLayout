package com.hxm.flowlayoutlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hxm on 2018/2/23.
 * 描述：
 */
public class FlowLayout extends BaseFlowLayout {
    private FlowAdapter adapter;
    private int multiSelectNum;//默认0没有选中事件,小于等于-1不限制选中数量，1为单选，大于1为多选
    private LayoutInflater mInflater;
    //选中的view index集合
    private LinkedList<Integer> selectedList = new LinkedList<>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseFlowLayout);
        multiSelectNum = ta.getInt(R.styleable.BaseFlowLayout_multiSelectNum, 0);
        ta.recycle();
    }

    /**
     * 设置adapter
     *
     * @param adapter FlowAdapter
     */
    public void setAdapter(FlowAdapter adapter) {
        setAdapter(adapter, -1);
    }

    /**
     * 设置adapter
     *
     * @param adapter FlowAdapter
     * @param posList 多选状态，默认选择的位置
     */
    public void setAdapter(FlowAdapter adapter, List<Integer> posList) {
        if (adapter == null)
            throw new NullPointerException("FlowAdapter is null!");
        selectedList.clear();
        this.adapter = adapter;
        int listSize = posList.size();
        if (listSize > 0 && multiSelectNum > 1) {
            if (listSize >= multiSelectNum) {
                listSize = multiSelectNum;
            }
            for (int i = 0; i < listSize; i++) {
                selectedList.addLast(posList.get(i));
            }
        }
        notifyDataChanged();
    }

    /**
     * 设置adapter
     *
     * @param adapter  FlowAdapter
     * @param position 单选，默认选中位置
     */
    public void setAdapter(FlowAdapter adapter, int position) {
        if (adapter == null)
            throw new NullPointerException("FlowAdapter is null!");
        selectedList.clear();
        this.adapter = adapter;
        if (position != -1 && multiSelectNum != 0) {
            selectedList.addFirst(position);
        }
        notifyDataChanged();
    }

    private void notifyDataChanged() {
        removeAllViews();
        int childCount = adapter.getCount();
        for (int i = 0; i < childCount; i++) {
            View view = adapter.getView(this, mInflater, i);
            if (selectedList.contains(i)) {
                adapter.onItemSelect(view, i);
            }
            dealSelect(view, i);
            addView(view);
        }
    }

    private void dealSelect(final View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.onItemClickListener != null) {
                    adapter.onItemClickListener.onClick(v, position);
                }
                if (multiSelectNum <= -1) {
                    //不限制选中数量
                    if (selectedList.contains(position)) {
                        //已经选中，再次点击取消选中状态
                        selectedList.remove(Integer.valueOf(position));
                        adapter.onItemUnSelect(v, position);
                    } else {
                        adapter.onItemSelect(v, position);
                        selectedList.add(position);
                    }
                } else if (multiSelectNum == 1) {
                    //单选
                    if (!selectedList.contains(position)) {
                        if (selectedList.size() > 0) {
                            int prePosition = selectedList.getFirst();
                            adapter.onItemUnSelect(getChildAt(prePosition), prePosition);
                        }
                        selectedList.clear();
                        selectedList.add(position);
                        adapter.onItemSelect(v, position);
                    }

                } else {
                    //多选
                    int size = selectedList.size();
                    if (size < multiSelectNum) {
                        if (selectedList.contains(position)) {
                            //已经选中，再次点击取消选中状态
                            selectedList.remove(Integer.valueOf(position));
                            adapter.onItemUnSelect(v, position);
                        } else {
                            adapter.onItemSelect(v, position);
                            selectedList.add(position);
                        }
                    } else {
                        if (selectedList.contains(position)) {
                            //已经选中，再次点击取消选中状态
                            selectedList.remove(Integer.valueOf(position));
                            adapter.onItemUnSelect(v, position);
                        } else {
                            adapter.onExceedsLimit(multiSelectNum);
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置选中数量
     *
     * @param num <=-1 不限制选中数量,
     *            0 没有选中事件,
     *            1 为单选,
     *            >1 为多选,
     *            默认0
     */
    public void setMultiSelectNum(int num) {
        this.multiSelectNum = num;
    }

    /**
     * 获取选中数量
     *
     * @return
     */
    public int getMultiSelectNum() {
        return multiSelectNum;
    }

    /**
     * 获取选中view的集合
     *
     * @return selectedList
     */
    public LinkedList<Integer> getSelectedList() {
        return selectedList;
    }
}
