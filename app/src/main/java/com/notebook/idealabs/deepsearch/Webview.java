package com.notebook.idealabs.deepsearch;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;*/

public class Webview extends AppCompatActivity {

    ProgressBar progressBar;
    WebView webView;
    String copiedURL = "";

    //private AdView mAdView;

    class webclient extends WebChromeClient{
        public void onProgressChanged(WebView view, int progress) {
            Webview.this.progressBar.setProgress(progress * 100);
        }
    }

    class download implements DownloadListener {
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Webview.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_webview);

        Configuration config = getResources().getConfiguration();

        /*MobileAds.initialize(this, "ca-app-pub-1364271166547745/6398053906");

        mAdView = (AdView) findViewById(R.id.browse);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);*/

        toolbar.setTitle((CharSequence) "");
        toolbar.setSubtitle((CharSequence) "");
        setSupportActionBar(toolbar);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.progressBar = (ProgressBar) findViewById(R.id.Loaded);
        this.webView = (WebView) findViewById(R.id.webview);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setBuiltInZoomControls(false);
        this.webView.getSettings().setUserAgentString("Android");
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (Webview.this.getResources().getConfiguration().orientation == 1) {
                    Webview.this.setRequestedOrientation(7);
                } else {
                    Webview.this.setRequestedOrientation(6);
                }
                toolbar.setSubtitle((CharSequence) url);
                Webview.this.copiedURL = url;
                Webview.this.progressBar.setVisibility(0);
            }

            public void onPageFinished(WebView view, String url) {
                toolbar.setTitle(view.getTitle());
                Webview.this.setRequestedOrientation(4);
                Webview.this.progressBar.setVisibility(8);
            }
        });
        this.webView.setWebChromeClient(new webclient());
        if (savedInstanceState != null) {
            this.webView.restoreState(savedInstanceState);
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.webView.loadUrl(extras.getString("webData"));
            } else {
                return;
            }
        }
        this.webView.setDownloadListener(new download());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Webview.this.copiedURL != null) {
                    ((ClipboardManager) Webview.this.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Copied Text", Webview.this.copiedURL));
                    Snackbar.make(view, "URL Copied : " + Webview.this.copiedURL, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.webView.saveState(outState);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.webView.canGoBack()) {
            return super.onKeyDown(keyCode, event);
        }
        this.webView.goBack();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_menu_id:
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Hey check out this new app at: https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                return true;
            case R.id.rate_us_menu_id:
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
                    return true;
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    return true;
                }
            case R.id.more_apps_menu_id:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://developer?id=7665922803421240272")));
                    return true;
                } catch (ActivityNotFoundException e2) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/dev?id=7665922803421240272")));
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
