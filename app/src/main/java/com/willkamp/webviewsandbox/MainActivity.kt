package com.willkamp.webviewsandbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.willkamp.webviewsandbox.ui.main.WebViewFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WebViewFragment())
                .commitNow()
        }
    }
}