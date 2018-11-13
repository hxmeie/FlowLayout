package com.hxm.floatlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hxm.flowlayoutlib.FlowAdapter;
import com.hxm.flowlayoutlib.FlowLayout;


/**
 * Created by hxm on 2018/2/24.
 * 描述：
 */
public class TagAdapter extends FlowAdapter {
    private String[] datas = {"bdgb", "bdgb", "bdgb", "adhmucc", "kkssvrfss", "adhmucc", "kkssvrfss",  "adhmucc", "kkssvrfss", "adfsdadddddddddddddddddddddddd","dddddddddddddddddddddddddddd", "bdgb", "adhmucc", "kkssvrfss", "ghe", "eea", "gjjjg", "hqxcbnmkuh", "idaeeedd", "jadafdasf", "fdfk"};
    private Context context;

    public TagAdapter(Context context) {
        this.context = context;

    }

    @Override
    public View getView(FlowLayout parent, LayoutInflater inflater, int position) {
        TextView view = (TextView) inflater.inflate(R.layout.tag_item, parent, false);
        view.setText(datas[position]);
        return view;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public void onItemSelect(View view, int position) {
        view.setSelected(true);
        Toast.makeText(context, "选中"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemUnSelect(View view, int position) {
        view.setSelected(false);
        Toast.makeText(context, "取消选中"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExceedsLimit(int limit) {
        Toast.makeText(context, "最多选中"+limit+"个", Toast.LENGTH_SHORT).show();
    }
}
