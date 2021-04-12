package dev.mrz3t4.literatureclub.Utils;

import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import dev.mrz3t4.literatureclub.R;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public class WebViewBuilder {

    public void webView(String url, Context context){
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(context.getResources().getColor(R.color.black))
                .setShowTitle(true)
                .build();

        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent);


        CustomTabsHelper.openCustomTab(context, customTabsIntent,
                Uri.parse(url),
                new WebViewFallback());
    }

}
