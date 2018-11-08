package com.hxm.floatlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hxm.flowlayoutlib.FlowLayout;

public class MainActivity extends AppCompatActivity {
    private FlowLayout flowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = findViewById(R.id.flow_layout);
        flowLayout.setMultiSelectNum(1);
        flowLayout.setAdapter(new TagAdapter(this));
    }
}
