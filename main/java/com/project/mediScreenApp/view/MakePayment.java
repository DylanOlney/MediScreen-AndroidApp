package com.project.mediScreenApp.view;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import com.project.mediScreenApp.R;

// This activity consists of a webView which loads a PayPal payment page.
// The URL for this page was obtained by requesting a payment 'button' from a PayPal business account.


public class MakePayment extends AppCompatActivity {


    private ProgressBar pBar;
    private WebView wView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        setTitle("Insurance Payment (PayPal)");
        pBar = (ProgressBar) findViewById(R.id.progressBar2);
        wView = findViewById(R.id.myWebView);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        pBar.setVisibility(View.VISIBLE);
        wView.setWebViewClient(new WebViewClient(){


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.equals("http://localhost/mediscreenweb/")){
                    Toast.makeText(MakePayment.this, "Payment Cancelled!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if (url.equals("http://localhost/mediscreenweb/php_pages/index.php")){
                    Toast.makeText(MakePayment.this, "Payment Accepted!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pBar.setVisibility(View.GONE);

            }
        });

        wView.loadUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=8Q92WCULKYGDU");

    }
}
