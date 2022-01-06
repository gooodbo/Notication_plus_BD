package com.example.kr2rpois;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class TextActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_layout);
        TextView tv = findViewById(R.id.textView32);
        String data = (String) getIntent().getSerializableExtra("Text");
        tv.setText(data);
    }
}
