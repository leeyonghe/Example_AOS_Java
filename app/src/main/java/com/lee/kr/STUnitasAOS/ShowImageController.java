package com.lee.kr.STUnitasAOS;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ShowImageController extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimage_layout);
        String image_url = getIntent().getStringExtra("image_url");
        Log.i("TAG", "image_url  >>>>>>>>>>  "+image_url);
        ImageView icon = findViewById(R.id.icon);

        Glide
        .with(this)
        .load(image_url)
        .fitCenter()
        .into(icon);

        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

}