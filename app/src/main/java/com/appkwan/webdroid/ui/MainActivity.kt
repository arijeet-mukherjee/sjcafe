package com.appkwan.webdroid.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appkwan.webdroid.R
import com.appkwan.webdroid.menumore.MoreMenuBottomSheetDialog
import com.appkwan.webdroid.menumore.MoreMenuClickListener
import com.appkwan.webdroid.menumore.MoreMenuItem
import com.appkwan.webdroid.utils.Constant
import com.github.loadingview.LoadingDialog
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.InterstitialAd
//import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    AdvancedWebView.Listener,
    BottomNavigationView.OnNavigationItemReselectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener,
    SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener,
    MoreMenuClickListener {

    private lateinit var moreBottomSheetDialog: MoreMenuBottomSheetDialog
    var moreMenuItemList = ArrayList<MoreMenuItem>()
    lateinit var emptyContainerConstraintLayout: ConstraintLayout
   // private lateinit var mInterstitialAd: InterstitialAd
    private var TAG = MainActivity::class.java.name
   // val dialog = LoadingDialog.get(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation_view.setOnNavigationItemSelectedListener(this)
        bottom_navigation_view.setOnNavigationItemReselectedListener(this)

        swipe_refresh_layout.setOnRefreshListener(this)
        webview.setListener(this, this)
        //dialog = LoadingDialog.get(this)
        var errorLayout = error_layout as ConstraintLayout
        var button = errorLayout.findViewById(R.id.retryButton) as Button
       // val dialog = LoadingDialog.get(this).show()
        /**
        //full page ad init
        MobileAds.initialize(this)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.interestitialAdId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        */
        button.setOnClickListener(this)

        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.webChromeClient = WebChromeClient()
        webview.settings.setSupportMultipleWindows(true)

        var webViewClient = com.appkwan.webdroid.utils.WebViewClient()
        webViewClient.improveWebViewPerformance(this, webview)

        webview.setWebChromeClient(object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                var newWebView = AdvancedWebView(this@MainActivity)
                val transport = resultMsg?.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()

                return true
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
            }
        })


        webview.loadUrl(getString(R.string.your_website_url))

        initMoreMenu()
        moreBottomSheetDialog = MoreMenuBottomSheetDialog(moreMenuItemList)
    }

    override fun onMoreMenuItemClicked(menuPosition: Int) {
        moreBottomSheetDialog.dialog?.dismiss()

        var typeOfUrl = moreMenuItemList[menuPosition].type
        var url = moreMenuItemList[menuPosition].url

        if (typeOfUrl.equals(Constant.TYPE.URL)) {
            webview.loadUrl(moreMenuItemList[menuPosition].url)
        } else if (typeOfUrl.equals(Constant.TYPE.WHATSUP)) {
            sendWhatsUpIntent(url)
        } else if (typeOfUrl.equals(Constant.TYPE.RATING)) {
            sendRateUsIntent(url)
        } else if (typeOfUrl.equals(Constant.TYPE.SHARE_APP)) {
            sendShareAppIntent(url)

        } else if (typeOfUrl.equals(Constant.TYPE.EMAIL)) {
            sendEmailIntent(url)

        } else if (typeOfUrl.equals(Constant.TYPE.CALL)) {
            sendCallIntent(url)
        }

    }

    private fun sendRateUsIntent(packageName: String) {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }

    }

    private fun sendEmailIntent(emailAddress: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setData(Uri.parse(emailAddress))
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
        startActivity(Intent.createChooser(intent, "Email via..."))
    }

    private fun sendCallIntent(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun sendShareAppIntent(url: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey check out the app at: https://play.google.com/store/apps/details?id=$url"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    private fun sendWhatsUpIntent(number: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    String.format(
                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                        number, ""
                    )
                )
            )
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.retryButton -> {
                webview.reload()
            }
        }
    }

    override fun onRefresh() {
        Log.e(TAG, "onRefresh()")
        webview.reload()
        swipe_refresh_layout.isRefreshing = false
        //showAd()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        webview.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        webview.onDestroy()
    }

    override fun onBackPressed() {
        if (!webview.onBackPressed()) return
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //this will handle any image, videos or file selection by intent
        webview.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onNavigationItemReselected(menuItem: MenuItem) {
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {

            R.id.action_home -> {
                //dialog.show()
                webview.loadUrl(getString(R.string.your_website_url))

            }

            R.id.action_apple -> {
                webview.loadUrl("https://sjcafe.net/programs/")
            }

            R.id.action_nike -> {
                webview.loadUrl("https://samcloudmedia.spacial.com/webwidgets/player/v4/300x160.html?sid=126348&rid=265680&startstation=false&theme=light&showBuyButton=never&token=54fcde6cdadda6ff91bf210809988e0c06422b11/")
            }

            R.id.action_blog -> {
                webview.loadUrl("https://sjcafe.net/bio/")
            }

//            R.id.action_beach -> {
//            }

            R.id.action_more -> {
                moreBottomSheetDialog.show(supportFragmentManager, "more_menu_bottom_sheet")
                return false
            }
        }

        return true
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        loader.smoothToShow()
        spin_kit.isVisible=true
        //val dialog = LoadingDialog.get(this).show()
       // dialog.show()
    }

    override fun onPageFinished(url: String?) {
        loader.smoothToHide()
        spin_kit.isVisible=false
        //dialog.hide()
        //hide error layout
        if (error_layout != null) error_layout.visibility = View.GONE

    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        //show blank
        webview.loadUrl("about:blank")
        //hide loader
        loader.smoothToHide()
        //show error layout
        if (error_layout != null) error_layout.visibility = View.VISIBLE
        Log.e(TAG, "error")
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {

    }

    override fun onExternalPageRequest(url: String?) {

    }
/**
    private fun showAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }
    }
*/
    private fun initMoreMenu() {


        var youtube = getDrawable(R.drawable.ic_privacy)?.let {
            MoreMenuItem(
                "Upfront & Personal",
                it,
                Constant.TYPE.URL,
                "https://sjcafe.net/upfront-personal/"
            )
        }
    if (youtube != null) {
        moreMenuItemList.add(youtube)
    }







        var menuItem7 = getDrawable(R.drawable.ic_email_black_24dp)?.let {
            MoreMenuItem(
                "Contact Us",
                it,
                Constant.TYPE.URL,
                "https://sjcafe.net/contact-us/"
            )
        }
    if (menuItem7 != null) {
        moreMenuItemList.add(menuItem7)
    }

        var menuItem1 = getDrawable(R.drawable.ic_share)?.let {
            MoreMenuItem(
                "Share App",
                it,
                Constant.TYPE.SHARE_APP,
                this.packageName.toString()
            )
        }
    if (menuItem1 != null) {
        moreMenuItemList.add(menuItem1)
    }

        var menuItem8 = getDrawable(R.drawable.ic_thumb_up_black_24dp)?.let {
            MoreMenuItem(
                "Rate Us",
                it,
                Constant.TYPE.RATING,
                this.packageName.toString()
            )
        }
    if (menuItem8 != null) {
        moreMenuItemList.add(menuItem8)
    }
    }
}
