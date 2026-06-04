package com.master.megaapp;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class MainActivity extends AppCompatActivity {

    // IMPORTANT: Apne Unity Dashboard par 'Interstitial_Android' ki spelling match kar lena bhai
    private String unityGameId = "6129135"; 
    private String adPlacementId = "Interstitial_Android";
    private boolean testMode = false; // Real Live Traffic and Revenue Mode
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Anti-Crash UI Container Layer
        FrameLayout rootLayout = new FrameLayout(this);
        rootLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 
                FrameLayout.LayoutParams.MATCH_PARENT));
        
        myWebView = new WebView(this);
        myWebView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, 
                FrameLayout.LayoutParams.MATCH_PARENT));
        
        rootLayout.addView(myWebView);
        setContentView(rootLayout);
        
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://multi-tool-mobile-app-678.created.app");

        // Initialization with Fail-Safe Diagnostics
        UnityAds.initialize(getApplicationContext(), unityGameId, testMode, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Unity Engine Init Success", Toast.LENGTH_SHORT).show());
                loadUnityAd();
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Init Failed: " + message, Toast.LENGTH_LONG).show());
            }
        });

        Toast.makeText(this, "Opening Multi-Tool Apps...", Toast.LENGTH_SHORT).show();
    }

    private void loadUnityAd() {
        UnityAds.load(adPlacementId, new IUnityAdsLoadListener() {
            @Override
            public void onAdLoaded(String placementId) {
                if (MainActivity.this.isFinishing()) return;
                UnityAds.show(MainActivity.this, adPlacementId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {}
                    @Override
                    public void onUnityAdsShowStart(String placementId) {}
                    @Override
                    public void onUnityAdsShowClick(String placementId) {}
                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {}
                });
            }

            @Override
            public void onAdFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                // Agar ID mismatched hogi toh screen par pata chal jayega
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Ad Load Failed: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
          }
          
