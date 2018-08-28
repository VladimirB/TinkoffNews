package com.app.tinkoff.news

import android.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.TextView
import org.joda.time.format.DateTimeFormat

@BindingAdapter("date")
fun setPublicationDate(textView: TextView, timestamp: Long?) {
    timestamp?.let {
        val dateFormatter = DateTimeFormat.forPattern("d MMM yyyy")
        if (it > 0) {
            textView.text = dateFormatter.print(it)
        }
    }
}

@Suppress("DEPRECATION")
@BindingAdapter("htmlText")
fun setContentText(textView: TextView, text: String?) {
    text?.let {
        val htmlText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(it)
        }
        textView.text = htmlText
    }
}