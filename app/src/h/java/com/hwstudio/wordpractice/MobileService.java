package com.hwstudio.wordpractice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.widget.FrameLayout;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;

public class MobileService {
    InterstitialAd interstitialAd;

    public void initBanner(Context getContext, FrameLayout getLayout, Display display) {
        HwAds.init(getContext);
        BannerView bannerView = new BannerView(getContext);
        bannerView.setAdId("p3pq7p3bnk");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        getLayout.addView(bannerView);
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);
    }

    public void initInter(Context getContext) {
        HwAds.init(getContext);
        interstitialAd = new InterstitialAd(getContext);
        interstitialAd.setAdId("h1gpqdtmna");
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }

    public void showInter(Activity activity) {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    public void rateApp(Context getContext) {
        try {
            Intent rateIntent = rateIntentForUrl("market://details?id=com.hwstudio.wordpractice.h");
            getContext.startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://appgallery.huawei.com/#/app/C103781139");
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
        shareMessage = shareMessage + "https://appgallery.huawei.com/#/app/C103781139";
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        getContext.startActivity(Intent.createChooser(intent, getContext.getString(R.string.chooseAppText)));
    }

    public void openPrivacyPolicy(Context getContext) {
        Intent rateIntent = rateIntentForUrl("https://wakemanck.github.io/Word-Practice/HMSVersionPrivacyPolicy.html");
        getContext.startActivity(rateIntent);
    }
}
