package com.hwstudio.wordpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    private final MobileService wsAd = new MobileService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Load ad
        wsAd.initInter(this);
    }

    @Override
    protected void onDestroy() {
        // Show ad
        wsAd.showInter();
        super.onDestroy();
    }
}