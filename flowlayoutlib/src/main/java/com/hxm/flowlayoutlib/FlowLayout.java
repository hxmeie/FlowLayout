package com.hxm.flowlayoutlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hxm on 2018/2/23.
 * 描述：
 */
public class FlowLayout extends BaseFlowLayout {
    private FlowAdapter adapter;
    private int limitNum;//限制选中数量
    private LayoutInflater mInflater;
    //选中的view index集合
    private LinkedList<Integer> selectedList = new LinkedList<>();
    private SelectType selectType = SelectType.NONE;//默认没有选中事件

    @SuppressLint("CustomViewStyleable")
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseFlowLayout);
        limitNum = ta.getInt(R.styleable.BaseFlowLayout_limitNum, 10);//默认10个
        int type = ta.getInt(R.styleable.BaseFlowLayout_selectType, 3);
        if (type == 3) {
            selectType = SelectType.NONE;
        } else if (type == 0) {
            selectType = SelectType.LIMIT;
        } else if (type == 1) {
            selectType = SelectType.SINGLE;
        } else if (type == 2) {
            selectType = SelectType.MULTI;
        }
        ta.recycle();
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 设置adapter，没有默认选中事件
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
     * @param posList 默认选择的位置
     */
    public void setAdapter(@NonNull FlowAdapter adapter, @NonNull List<Integer> posList) {
        this.adapter = adapter;
        refreshLayout(posList);
    }

    /**
     * 设置adapter
     *
     * @param adapter  FlowAdapter
     * @param position 默认选中位置
     */
    public void setAdapter(@NonNull FlowAdapter adapter, int position) {
        this.adapter = adapter;
        List<Integer> posList = new ArrayList<>();
        if (position >= 0) {
            posList.add(position);
        }
        refreshLayout(posList);
    }

    /**
     * 更新界面
     *
     * @param posList 默认选中位置
     */
    private void refreshLayout(List<Integer> posList) {
        removeAllViews();
        initSelectedList(posList);
        int childCount = adapter.getCount();
        for (int i = 0; i < childCount; i++) {
            View view = adapter.getView(this, mInflater, i);
            if (selectedList.contains(i)) {
                adapter.onItemSelect(view, i);
            }
            itemSelect(view, i);
            addView(view);
        }
    }

    /**
     * 初始化selectedList中的值
     *
     * @param posList 选中的position
     */
    private void initSelectedList(@NonNull List<Integer> posList) {
        selectedList.clear();
        int listSize = posList.size();
        if (listSize > 0) {
            if (selectType == SelectType.SINGLE) {
                listSize = 1;
            } else if (selectType == SelectType.LIMIT) {
                if (limitNum <= 0) {
                    listSize = 0;
                } else if (listSize > limitNum) {
                    listSize = limitNum;
                }
            } else if (selectType == SelectType.NONE) {
                listSize = 0;
            }
            for (int i = 0; i < listSize; i++) {
                selectedList.addLast(posList.get(i));
            }
        }
    }

    /**
     * 处理选中逻辑，type优先级高于limitNum
     */
    private void itemSelect(final View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.onItemClickListener != null) {
                    adapter.onItemClickListener.onClick(v, position);
                }
                if (selectType == SelectType.SINGLE) {
                    //单选
                    selectSingle(view, position);
                } else if (selectType == SelectType.MULTI) {
                    //多选
                    selectMulti(view, position);
                } else if (selectType == SelectType.LIMIT) {
                    //限制选中数量
                    selectLimit(view, position);
                }
            }
        });
    }

    /**
     * 处理单选
     *
     * @param position 点击的position
     */
    private void selectSingle(final View view, final int position) {
        if (!selectedList.contains(position)) {
            if (selectedList.size() > 0) {
                int prePosition = selectedList.getFirst();
                adapter.onItemUnSelect(getChildAt(prePosition), prePosition);
            }
            selectedList.clear();
            selectedList.add(position);
            adapter.onItemSelect(view, position);
        }
    }

    /**
     * 处理多选
     *
     * @param position 点击的position
     */
    private void selectMulti(final View view, final int position) {
        if (selectedList.contains(position)) {
            //已经选中，再次点击取消选中状态
            selectedList.remove(Integer.valueOf(position));
            adapter.onItemUnSelect(view, position);
        } else {
            adapter.onItemSelect(view, position);
            selectedList.add(position);
        }
    }

    /**
     * 处理限制选中数量
     *
     * @param position 点击的position
     */
    private void selectLimit(final View view, final int position) {
        if (limitNum <= 0)
            return;
        int size = selectedList.size();
        if (size < limitNum) {
            selectMulti(view, position);
        } else {
            if (selectedList.contains(position)) {
                //已经选中，再次点击取消选中状态
                selectedList.remove(Integer.valueOf(position));
                adapter.onItemUnSelect(view, position);
            } else {
                adapter.onExceedsLimit(limitNum);
            }
        }
    }

    /**
     * 获取选中类型
     */
    public SelectType getSelectType() {
        return selectType;
    }

    /**
     * 设置选中类型
     *
     * @param type
     */
    public void setSelectType(SelectType type) {
        this.selectType = type;
    }

    /**
     * 获取选中数量
     *
     * @return
     */
    public int getLimitNum() {
        return limitNum;
    }

    /**
     * 设置选中数量
     *
     * @param num 多选选中数量
     */
    public void setLimitNum(int num) {
        this.limitNum = num;
    }

    /**
     * LIMIT:    限制选中数量,数量为limitNum
     * SINGLE:   单选
     * MULTI:    多选
     * NONE:     没有选中事件
     */
    public enum SelectType {
        LIMIT, SINGLE, MULTI, NONE
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
