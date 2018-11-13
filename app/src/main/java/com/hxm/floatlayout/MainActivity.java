package com.hxm.floatlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hxm.flowlayoutlib.FlowLayout;

public class MainActivity extends AppCompatActivity {
    private FlowLayout flowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = findViewById(R.id.flow_layout);
        flowLayout.setAdapter(new TagAdapter(this), 5);
    }
}
