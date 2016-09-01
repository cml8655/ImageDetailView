package com.cml.framework.imagedetailview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cml.framework.imagedetailview.widget.TaiChiProgressView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaiChiProgressView taiChiProgressView = (TaiChiProgressView) findViewById(R.id.taichiView);
        taiChiProgressView.setMaxProgress(100);
        taiChiProgressView.setProgress(5);
        taiChiProgressView.setSize(250);

        taiChiProgressView.setBgColor(Color.DKGRAY);
        taiChiProgressView.setLeftTaichiColor(Color.GREEN);
        taiChiProgressView.setRightTaichiColor(Color.BLUE);
    }
}
