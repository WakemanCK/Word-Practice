package com.hwstudio.wordpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private final MobileService msAd = new MobileService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Load ad
        msAd.initInter(getApplicationContext());

        TextView helpText = findViewById(R.id.helpTextView);
        SpannableStringBuilder helpString = new SpannableStringBuilder(getString(R.string.helpText01));

        addHeadline(helpString, getString(R.string.generalTextView));
        helpString.append(getString(R.string.helpText02));
        addColor(helpString, getString(R.string.loadExamples));
        helpString.append(getString(R.string.helpText03));
        addColor(helpString, getString(R.string.finishButton));
        helpString.append(getString(R.string.helpText04));
        addColor(helpString, getString(R.string.saveButton));
        helpString.append(getString(R.string.helpText05));
        addColor(helpString, getString(R.string.loadButton));
        helpString.append(getString(R.string.helpText06));
        addColor(helpString, getString(R.string.helpText06b));
        helpString.append(getString(R.string.helpText06c));
        addColor(helpString, getString(R.string.helpText06d));
        helpString.append(getString(R.string.helpText06e));
        addColor(helpString, getString(R.string.helpText06f));
        helpString.append(getString(R.string.helpText06g));
        addColor(helpString, getString(R.string.helpText06h));
        helpString.append(getString(R.string.helpText06i));
        addHeadline(helpString, getString(R.string.settingsMenu));
        helpString.append(getString(R.string.helpText07));
        helpString.setSpan(new ImageSpan(this, R.drawable.ic_round_play_circle_24), helpString.length() - 1, helpString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpString.append(getString(R.string.helpText08));
        helpString.setSpan(new ImageSpan(this, R.drawable.ic_round_pause_circle_24), helpString.length() - 1, helpString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpString.append(getString(R.string.helpText09));
        helpString.setSpan(new ImageSpan(this, R.drawable.ic_round_stop_circle_24), helpString.length() - 1, helpString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpString.append(getString(R.string.helpText10));
        addColor(helpString, getString(R.string.settingsMenu));
        helpString.append(getString(R.string.helpText11));
        addColor(helpString, getString(R.string.settingsMenu));
        helpString.append(getString(R.string.helpText12));
        addHeadline(helpString, getString(R.string.tipsText));
        helpString.append(getString(R.string.helpText13));
        helpString.append(getString(R.string.helpText14));
        addColor(helpString, getString(R.string.helpText14b));
        helpString.append(getString(R.string.helpText14c));
        addColor(helpString, getString(R.string.helpText14d));
        helpString.append(getString(R.string.helpText14e));
        helpString.append(getString(R.string.helpText15));
        helpString.setSpan(new ImageSpan(this, R.drawable.ic_baseline_flip_to_front_24), helpString.length() - 1, helpString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpString.append(getString(R.string.helpText16));
        helpText.setText(helpString, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void onDestroy() {
        // Show ad
        msAd.showInter(this);
        super.onDestroy();
    }

    private void addHeadline(SpannableStringBuilder getString, String headlineString) {
        int index = getString.length();
        getString.append(headlineString);
        ForegroundColorSpan headlineColor = new ForegroundColorSpan(getResources().getColor(R.color.purple_500));
        getString.setSpan(headlineColor, index, getString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getString.setSpan(new RelativeSizeSpan((float) 9 / 7), index, getString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan headlineBold = new StyleSpan(Typeface.BOLD);
        getString.setSpan(headlineBold, index, getString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void addColor(SpannableStringBuilder getString, String headlineString) {
        int index = getString.length();
        getString.append(headlineString);
        ForegroundColorSpan headlineColor = new ForegroundColorSpan(getResources().getColor(R.color.purple_500));
        getString.setSpan(headlineColor, index, getString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void returnToMain(View view) {
        finish();
    }
}