package com.willkamp.webviewsandbox.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import com.willkamp.webviewsandbox.R

class WebViewFragment : Fragment() {

    lateinit var webView: WebView

    private val activityResultContract =
        object : ActivityResultContract<WebChromeClient.FileChooserParams, Array<Uri>?>() {
            var callback: ValueCallback<Array<Uri>>? = null
            override fun createIntent(
                context: Context,
                input: WebChromeClient.FileChooserParams
            ): Intent {
                return input.createIntent()
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Array<Uri>? {
                return WebChromeClient.FileChooserParams.parseResult(resultCode, intent)
            }
        }

    private val activityResultLauncher = registerForActivityResult(activityResultContract) {
        Log.d(TAG, "invoking file picker callback with ${it?.size} results")
        activityResultContract.callback?.onReceiveValue(it)
        activityResultContract.callback = null
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            Log.d(TAG, "launching file picker")
            activityResultContract.callback = filePathCallback
            activityResultLauncher.launch(fileChooserParams)
            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webView)
        webView.webChromeClient = webChromeClient
        if (savedInstanceState == null) {
            webView.loadUrl("file:///android_asset/wv_data.html")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "saving webview state")
        webView.saveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            Log.d(TAG, "restoring webview state")
            webView.restoreState(it)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "pausing webview $webView")
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "resuming webview $webView")
        webView.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "destroying webview $webView")
        webView.destroy()
    }

    companion object {
         val TAG = WebViewFragment::class.java.simpleName
    }
}
