package com.appkwan.webdroid.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import com.appkwan.webdroid.R
import im.delight.android.webview.AdvancedWebView
import java.util.*

class WebViewClient {
    fun improveWebViewPerformance(context: Context, webview: AdvancedWebView) {
        webview.setWebViewClient(object : android.webkit.WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                if (url.startsWith("tel:")) {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse(url)
                    )
                    context.startActivity(intent)
                    return true
                }

                if (url.startsWith("mailto:")) {
                    val i = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                    context.startActivity(i)
                    return true
                }

                //TODO: comment out this below code if you want to send external link to browser
//                if(!url.contains(context.getString(R.string.your_website_url))){
//                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    context.startActivity(i)
//                    return true
//                }

                if (url.contains("https://www.google.com/maps/")) {
                    val IntentUri = Uri.parse(url)
                    val mapIntent = Intent(Intent.ACTION_VIEW, IntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")

                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent)
                    }
                    return true
                }

                if (url.startsWith("https://www.google.com/calendar")) {

                    val cal = Calendar.getInstance()
                    val intent = Intent(Intent.ACTION_EDIT)
                    intent.type = "vnd.android.cursor.item/event"
                    intent.putExtra("allDay", false)
                    intent.putExtra("rrule", "FREQ=YEARLY")
                    intent.putExtra("beginTime", cal.getTimeInMillis())
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    context.startActivity(intent)
                    view.loadUrl(url)
                    return true
                }

                return false
            }
        })

    }
}