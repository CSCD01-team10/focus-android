package org.mozilla.focus.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mozilla.components.browser.session.Session
import org.mozilla.focus.R
import org.mozilla.focus.ext.savedWebViewState
import org.mozilla.focus.web.GeckoWebViewProvider
import org.mozilla.geckoview.GeckoSession


class HistoryFragment : BottomSheetDialogFragment() {

    companion object {
        const val FRAGMENT_TAG = "history"
        private const val ARGUMENT_SESSION_UUID = "sessionUUID"

        fun create(currentSession: Session?): HistoryFragment {
            Log.d("HistoryFragment.kt", "=== HistoryFragment create() ===")
            Log.d("HistoryFragment.kt", "=== currentSession $currentSession ===")

            // get the sessionState
            val sessionState = currentSession?.savedWebViewState?.getParcelable<GeckoSession.SessionState>(GeckoWebViewProvider.GECKO_SESSION)

            Log.d("HistoryFragment.kt", "=== sessionState $sessionState ===")
            if (sessionState!= null) {
                val historySize = sessionState.size
                val historyIterator = sessionState.iterator()
                while (historyIterator.hasNext()) {
                    // Get list of history items
                    Log.d("HistoryFragment.kt", historyIterator.next().toString())
                }
            }

            val fragment = HistoryFragment()
            val arguments = Bundle()
            fragment.arguments = arguments

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
        View = inflater.inflate(R.layout.fragment_history, container, false)
}