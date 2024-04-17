package com.mercedesbenz.loadinganimation.models

import android.os.Parcelable
import com.mercedesbenz.loadinganimation.App
import com.mercedesbenz.loadinganimation.R
import com.mercedesbenz.loadinganimation.utils.Status
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadType(
    val downloadId: Long,
    val type: Int,
    var status: Status = Status.NOT_SET
) : Parcelable {
    fun toStrings(): DownloadStrings? {
        if (App.context == null) return  null
        return when (type) {
            1 -> DownloadStrings(
                title = App.context!!.getString(R.string.option_glide),
                description = App.context!!.getString(R.string.notification_description, App.context!!.getString(R.string.name_glide))
            )

            2 -> DownloadStrings(
                title = App.context!!.getString(R.string.option_udacity),
                description = App.context!!.getString(R.string.notification_description, App.context!!.getString(R.string.name_udacity))
            )

            3 -> DownloadStrings(
                title = App.context!!.getString(R.string.option_retrofit),
                description = App.context!!.getString(R.string.notification_description, App.context!!.getString(R.string.name_retrofit))
            )
            else -> null
        }

    }
}


data class DownloadStrings(
    val description: String,
    val title: String,
)