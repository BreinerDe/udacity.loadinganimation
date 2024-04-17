package com.mercedesbenz.loadinganimation.utils

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("Status")
fun bindTextViewToStatus(textView: TextView, status: Status) {
    textView.text = status.statusText
    textView.setTextColor(status.statusColor)
    textView.setTypeface(null, Typeface.BOLD)
}