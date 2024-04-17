package com.mercedesbenz.loadinganimation.activities

import android.app.NotificationManager
import android.os.Bundle
import com.google.gson.Gson
import com.mercedesbenz.loadinganimation.Constants
import com.mercedesbenz.loadinganimation.databinding.ActivityDetailBinding
import com.mercedesbenz.loadinganimation.models.DownloadType

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var downloadType: DownloadType
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeNotificationManager()
        cancelNotification()
        setupViews()
        setupActionBar()
        setupDownloadType()
        setClickListeners()
    }

    private fun initializeNotificationManager() {
        notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
    }

    private fun cancelNotification() {
        notificationManager.cancel(Constants.NOTIFICATION_DEFAULT_ID)
    }

    private fun setupViews() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupDownloadType() {
        val json: String? = sharedPreferences.getString(Constants.SHAREDPREF_DOWNLOAD_TYPE, null)
        json?.let {
            downloadType = Gson().fromJson(it, DownloadType::class.java)
            binding.downloadType = downloadType
        }
    }

    private fun setClickListeners() {
        binding.fabBack.setOnClickListener {
            finish()
        }
    }
}
