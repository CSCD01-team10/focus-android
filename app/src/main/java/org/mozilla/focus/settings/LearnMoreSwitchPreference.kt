package org.mozilla.focus.settings

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.TextView
import mozilla.components.browser.session.Session
import org.mozilla.focus.R
import org.mozilla.focus.ext.components
import android.util.Log
import org.mozilla.focus.activity.InfoActivity
import org.mozilla.focus.telemetry.TelemetryWrapper
import org.mozilla.focus.utils.SupportUtils

abstract class LearnMoreSwitchPreference(context: Context?, attrs: AttributeSet?) :
    SwitchPreferenceCompat(context, attrs) {

    init {
        layoutResource = R.layout.preference_switch_learn_more
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)

        getDescription()?.let {
            val summaryView = holder!!.findViewById(android.R.id.summary) as TextView
            summaryView.text = it
            summaryView.visibility = View.VISIBLE
        }

        val learnMoreLink = holder!!.findViewById(R.id.link) as TextView
        learnMoreLink.paintFlags = learnMoreLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        learnMoreLink.setTextColor(ContextCompat.getColor(context, R.color.colorAction))
        learnMoreLink.setOnClickListener {
            // This is a hardcoded link: if we ever end up needing more of these links, we should
            // move the link into an xml parameter, but there's no advantage to making it configurable now.
            val url = getLearnMoreUrl()
            val intent = InfoActivity.getIntentFor(context!!,
                    url, context.getString(R.string.enable_search_suggestion_subtitle_learnmore))
            context.startActivity(intent)
        }

        val backgroundDrawableArray =
            context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        val backgroundDrawable = backgroundDrawableArray.getDrawable(0)
        backgroundDrawableArray.recycle()
        learnMoreLink.background = backgroundDrawable
    }

    open fun getDescription(): String? = null

    abstract fun getLearnMoreUrl(): String
}
