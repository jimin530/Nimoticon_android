package com.jimin.selfgif;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.jimin.selfgif.SaveGif.AnimatedGifImageView;
import com.jimin.selfgif.SaveGif.AnimatedGifImageView.TYPE;


public class PopupMakeGifActivity extends Activity {

    AnimatedGifImageView animatedGifImageView;
    ImageButton btn_please;
    ImageButton btn_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popupmakegif_activity);
        animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
        try {
            animatedGifImageView.viewgif(PathClass.click_makegifroot, TYPE.STREACH_TO_FIT);
        } catch (Exception e) {

        }

        btn_share = (ImageButton) findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/gif");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(PathClass.click_makegifroot));
                startActivity(Intent.createChooser(shareIntent, "공유하기"));
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
