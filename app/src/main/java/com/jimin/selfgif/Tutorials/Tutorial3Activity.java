package com.jimin.selfgif.Tutorials;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.jimin.selfgif.R;

public class Tutorial3Activity extends Activity {

    ImageButton btn_end_tutorial3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial3);

        btn_end_tutorial3 = (ImageButton) findViewById(R.id.btn_end_tutorial3);
        btn_end_tutorial3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}