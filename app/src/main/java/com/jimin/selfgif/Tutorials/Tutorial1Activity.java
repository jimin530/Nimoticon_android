package com.jimin.selfgif.Tutorials;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.jimin.selfgif.R;

public class Tutorial1Activity extends Activity {

    ImageButton btn_end_tutorial1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial1);

        btn_end_tutorial1 = (ImageButton) findViewById(R.id.btn_end_tutorial1);
        btn_end_tutorial1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}