package com.jenny.deara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class PrivacyWebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_web_view)

        val webView = findViewById<WebView>(R.id.webView1)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.notion.so/54e15dcbe37c4f09a5e9392228a56f7d")
    }
}