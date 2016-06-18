package com.jimin.selfgif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jimin.selfgif.Camera.CameraViewActivity;
import com.jimin.selfgif.SaveGif.AnimatedGifImageView;
import com.jimin.selfgif.SaveGif.AnimatedGifImageView.TYPE;


public class PopupGifActivity extends Activity {

    AnimatedGifImageView animatedGifImageView;
    Button btn_please;
    Button btn_selectimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupgif_activity);
        animatedGifImageView = ((AnimatedGifImageView)findViewById(R.id.animatedGifImageView));
        try {
            //animatedGifImageView.viewgif(clickroot, TYPE.FIT_CENTER);
            //animatedGifImageView.setAnimatedGif(R.drawable.animated_gif, TYPE.FIT_CENTER);
            animatedGifImageView.setAnimatedGif(SelectActivity.click_gifresource, TYPE.FIT_CENTER);
        }
        catch (Exception e)
        {

        }

        btn_selectimg = (Button) findViewById(R.id.btn_selectimg);
        btn_selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(PopupGifActivity.this, MainActivity.class);

                SelectActivity.click_gifscene1 = SelectActivity.first_scene_number + 2*(SelectActivity.click_gifresource-SelectActivity.first_resource_number);
                SelectActivity.click_gifscene2 = SelectActivity.click_gifscene1+1;

                /*Intent i = new Intent(PopupGifActivity.this, CameraViewActivity.class);
                startActivity(i);*/

                Intent i = new Intent(getApplicationContext(), CameraViewActivity.class);
                startActivity(i);

                finish();
            }
        });

        btn_please = (Button) findViewById(R.id.btn_please);
        btn_please.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
