package com.example.android.icummenical.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.icummenical.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN

        );
        setContentView(R.layout.activity_splash_screen);
        //Abrindo a Thread
        Thread timeThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3700); // Colocando a thread pra dormir
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        }; // fechando a thread
        timeThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}


