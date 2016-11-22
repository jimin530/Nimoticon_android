package com.jimin.selfgif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.jimin.selfgif.Camera.CameraViewActivity;
import com.jimin.selfgif.SaveGif.AnimatedGifImageView;
import com.jimin.selfgif.SaveGif.AnimatedGifImageView.TYPE;


public class PopupGifActivity extends Activity {

    AnimatedGifImageView animatedGifImageView;
    ImageButton btn_please;
    ImageButton btn_selectimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupgif_activity);
        animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
        try {
            animatedGifImageView.setAnimatedGif(PathClass.click_gifresource, TYPE.FIT_CENTER);
        } catch (Exception e) {

        }

        btn_selectimg = (ImageButton) findViewById(R.id.btn_selectimg);
        btn_selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PathClass.click_gifscene1 = PathClass.first_scene_number + 2 * (PathClass.click_gifresource - PathClass.first_resource_number);
                PathClass.click_gifscene2 = PathClass.click_gifscene1 + 1;

                /*if (PathClass.fromcamera) {
                    Intent i = new Intent(getApplicationContext(), CameraViewActivity.class);
                    startActivity(i);
                }*/
                finish();
            }
        });

        btn_please = (ImageButton) findViewById(R.id.btn_please);
        btn_please.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
