package com.hwstudio.wordpractice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MobileService {
    private InterstitialAd interstitialAd;

    public void initBanner(Context getContext, FrameLayout getLayout, Display getDisplay) {
        AdView adView;
        MobileAds.initialize(getContext, initializationStatus -> {
        });
        adView = new AdView(getContext);
        adView.setAdUnitId("ca-app-pub-1067337728169403/5258231779");
        getLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getDisplay.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext, adWidth);
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    public void initInter(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, "ca-app-pub-1067337728169403/5557614670", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd getAd) {
                interstitialAd = getAd;
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        interstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                interstitialAd = null;
            }
        });
    }

    public void showInter(Activity activity) {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
        }
    }

    public void rateApp(Context getContext) {
        try {
            Intent rateIntent = rateIntentForUrl("market://details?id=com.hwstudio.wordpractice.g");
            getContext.startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=com.hwstudio.wordpractice.g");
            getContext.startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String urlString) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        intent.addFlags(flags);
        return intent;
    }

    public void shareApp(Context getContext) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getContext.getString(R.string.app_name));
        String shareMessage = getContext.getString(R.string.shareMessageText);
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.hwstudio.wordpractice.g";
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        getContext.startActivity(Intent.createChooser(intent, getContext.getString(R.string.chooseAppText)));
    }
}
