package org.mozilla.focus.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mozilla.components.browser.session.Session
import org.mozilla.focus.R
import org.mozilla.focus.ext.components
import org.mozilla.focus.web.GeckoWebViewProvider
import org.mozilla.focus.web.IWebView
import org.mozilla.focus.web.WebViewProvider


class HistoryFragment : BottomSheetDialogFragment() {

    companion object {
        const val FRAGMENT_TAG = "history"
        private const val ARGUMENT_SESSION_UUID = "sessionUUID"

        fun create(webView: IWebView): HistoryFragment? {
            val geckoWebView = webView as GeckoWebViewProvider.GeckoWebView? ?: return null
            val provider = (WebViewProvider.engine as GeckoWebViewProvider?) ?: return null

            val historyList = provider.sessionHistoryMap[geckoWebView.session]
            historyList?.forEach {
                Log.d("TEST", it.uri)
            }
            return HistoryFragment()
        }
    }

    lateinit var session : Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.components?.sessionManager?.selectedSession?.apply {
            session = this
        } ?: dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
        View = inflater.inflate(R.layout.fragment_history, container, false)
}