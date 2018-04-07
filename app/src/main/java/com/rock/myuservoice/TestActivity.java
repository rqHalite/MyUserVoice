package com.rock.myuservoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);

        TextView tv = new TextView(this);

        Intent intent = new Intent();
        String msg = intent.getStringExtra("msg");
        tv.setText(msg);
//        Intent intent = getIntent();
//        if (null != intent) {
//            Bundle bundle = getIntent().getExtras();
//            String msg =  bundle.getString("msg");
//            tv.setText(msg);
//        }
        addContentView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }
}
