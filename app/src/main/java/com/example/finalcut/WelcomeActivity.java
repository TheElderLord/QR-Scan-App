package com.example.finalcut;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
     ImageView img,imageView;
     TextView textView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        img = findViewById(R.id.nomadimg);
        img.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        imageView= findViewById(R.id.copyright);
        imageView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        textView = findViewById(R.id.copytext);
        textView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                //set the new Content of your activity
                Intent a = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(a);
            }
        }.start();
    }
}