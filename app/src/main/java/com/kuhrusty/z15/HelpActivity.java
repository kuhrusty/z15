package com.kuhrusty.z15;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Copied from MorbadScorepad; earlier comment left intact for posterity:
 *
 * <p>Arghh!!  I just want to display some HTML help files bundled with the app.
 * The amount of grief it took to get to this point is insane.
 */
public class HelpActivity extends AppCompatActivity {
    private static final String LOGBIT = "HelpActivity";

    public static final String INTENT_URL = "HelpActivity.helpURL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        WebView wv = findViewById(R.id.webView);
        //  enable navigation, because we may have links in our help documents
        wv.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view,
                                                              String url) {
                Log.d(LOGBIT, "shouldOverrideUrlLoading(view, " + url + ")");
                if (url.startsWith("file:///android_asset/")) return false;

                //  from
                //  http://tutorials.jenkov.com/android/android-web-apps-using-android-webview.html
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });

        String url = null;
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra(INTENT_URL);
        }
        if (url != null) {
            wv.loadUrl(url);
        }
    }

    /**
     * Calls finish().
     */
    public void done(@Nullable View view) {
        finish();
    }
}
