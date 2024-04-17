package com.mercedesbenz.loadinganimation.messaging

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mercedesbenz.loadinganimation.Constants
import com.mercedesbenz.loadinganimation.models.DownloadType
import com.mercedesbenz.loadinganimation.utils.Status
import com.mercedesbenz.loadinganimation.utils.sendNotification
import com.mercedesbenz.loadinganimation.utils.statusToStr
import timber.log.Timber

object Repository {
    private val _repIsDownloadCompleted = MutableLiveData<Boolean?>()
    val repIsDownloadCompleted: LiveData<Boolean?>
        get() = _repIsDownloadCompleted

    fun setDownloadIsCompleted(isCompleted: Boolean) {
        _repIsDownloadCompleted.value = isCompleted
    }
}

class LoadingAppBroadcastReceiver : BroadcastReceiver() {

    @SuppressLint("Range")
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        Repository.setDownloadIsCompleted(true)

        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as? DownloadManager ?: return
        val query = DownloadManager.Query().apply {
            setFilterById(id)
        }
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst() && cursor.getColumnIndex(DownloadManager.COLUMN_STATUS) >= 0) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            cursor.close()
            Timber.i("Download status for id $id: ${statusToStr(status)}")

            val sharedPreferences = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
            val json: String? = sharedPreferences.getString(Constants.SHAREDPREF_DOWNLOAD_TYPE, null)
            json?.let { jsonStr ->
                val downloadType = Gson().fromJson(jsonStr, DownloadType::class.java)
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        Timber.i("Download success: $id")
                        handleDownloadSuccess(downloadType, sharedPreferences, context)
                    }
                    DownloadManager.STATUS_FAILED -> {
                        Timber.w("Download failed")
                        handleDownloadFailure(downloadType, sharedPreferences, context)
                    }
                }
            }
        }

        when (intent.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> handleDownloadComplete()
        }
    }

    private fun handleDownloadSuccess(downloadType: DownloadType, sharedPreferences: SharedPreferences, context: Context) {
        downloadType.status = Status.SUCCESS
        sharedPreferences.edit().putString(Constants.SHAREDPREF_DOWNLOAD_TYPE, Gson().toJson(downloadType)).apply()
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as? NotificationManager
        notificationManager?.sendNotification(context.applicationContext, downloadType)
    }

    private fun handleDownloadFailure(downloadType: DownloadType, sharedPreferences: SharedPreferences, context: Context) {
        downloadType.status = Status.FAILED
        sharedPreferences.edit().putString(Constants.SHAREDPREF_DOWNLOAD_TYPE, Gson().toJson(downloadType)).apply()
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as? NotificationManager
        notificationManager?.sendNotification(context.applicationContext, downloadType)
    }

    private fun handleDownloadComplete() {
        Timber.i("Download complete!")
    }
}
