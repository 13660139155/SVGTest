package com.example.hy.svgtest;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hy.svgtest.R;

public class MainActivity extends AppCompatActivity {

    ImageView ivPath, ivSystem, ivSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivPath = findViewById(R.id.iv_path);
        ivSystem = findViewById(R.id.iv_system);
        ivSearch = findViewById(R.id.iv_search);
        Drawable drawable = ivSystem.getDrawable();
        if(drawable instanceof Animatable){
            ((Animatable)drawable).start();
        }
        ivPath = findViewById(R.id.iv_path);
        ivPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = ivPath.getDrawable();
                if(drawable instanceof Animatable){
                    ((Animatable)drawable).start();
                }
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivSearch.getDrawable() instanceof Animatable){
                    ((Animatable)ivSearch.getDrawable()).start();
                }
            }
        });
    }
}
