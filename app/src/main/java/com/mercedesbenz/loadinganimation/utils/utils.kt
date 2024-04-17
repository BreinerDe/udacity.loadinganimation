package com.mercedesbenz.loadinganimation.utils

import android.graphics.Color
import com.mercedesbenz.loadinganimation.App
import com.mercedesbenz.loadinganimation.Constants
import com.mercedesbenz.loadinganimation.R

enum class URL(val url: String) {
    UDACITY(Constants.URL_UDACITY),
    GLIDE(Constants.URL_GLIDE),
    RETROFIT(Constants.URL_RETROFIT)
}

enum class Status(val statusText: String, val statusColor: Int) {
    SUCCESS("Successfully downloaded", App.context?.getColor(R.color.successGreen) ?: Color.GRAY),
    FAILED("Download failed", App.context?.getColor(R.color.failureRed) ?: Color.GRAY),
    NOT_SET("not set", Color.GRAY)

}

fun getUrl(selection : Int) : String? {
    return when(selection) {
        1-> URL.GLIDE.url
        2-> URL.UDACITY.url
        3-> URL.RETROFIT.url
        else -> null
    }
}

fun statusToStr(status : Int) : String {
    return when(status) {
        1 -> "pending"
        2 -> "running"
        4 -> "paused"
        8 -> "success"
        16 -> "failed"
        else -> "other"
    }
}
