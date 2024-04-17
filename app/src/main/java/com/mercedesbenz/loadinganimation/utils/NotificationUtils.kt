package com.mercedesbenz.loadinganimation.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.mercedesbenz.loadinganimation.Constants
import com.mercedesbenz.loadinganimation.R
import com.mercedesbenz.loadinganimation.activities.DetailActivity
import com.mercedesbenz.loadinganimation.models.DownloadType


fun NotificationManager.sendNotification(applicationContext: Context, downloadType: DownloadType) {
    val downloadDetailsIntent = Intent(applicationContext, DetailActivity::class.java)
    val downloadDetailsPendingIntent = PendingIntent.getActivity(applicationContext, Constants.NOTIFICATION_DEFAULT_ID, downloadDetailsIntent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val downloadImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.baseline_cloud_download_24)
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(null as Bitmap?)

    val builder = NotificationCompat.Builder(
        applicationContext, Constants.CHANNEL_ID,
    )
        .setSmallIcon(R.drawable.baseline_cloud_download_24)
        .setContentTitle(downloadType.toStrings()?.title)
        .setContentText(downloadType.toStrings()?.description)
        .setStyle(bigPicStyle)
        .setLargeIcon(downloadImage)
        .setContentIntent(downloadDetailsPendingIntent)
        .setAutoCancel(true)
        .addAction(R.drawable.baseline_cloud_download_24, applicationContext.getString(R.string.notification_button), downloadDetailsPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(Constants.NOTIFICATION_DEFAULT_ID, builder.build())




}

