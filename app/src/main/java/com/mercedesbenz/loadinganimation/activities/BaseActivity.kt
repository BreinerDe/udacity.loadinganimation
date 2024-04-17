package com.mercedesbenz.loadinganimation.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mercedesbenz.loadinganimation.Constants
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {

    val sharedPreferences: SharedPreferences by lazy { getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        checkNotificationPermission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            handleNotificationPermissionResult(isGranted)
        }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermissionGranted = true
        } else {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun handleNotificationPermissionResult(isGranted: Boolean) {
        hasNotificationPermissionGranted = isGranted
        if (!isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                showNotificationPermissionRationale()
            } else {
                showSettingDialog()
            }
        }
    }

    private fun showSettingDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage("Notification permission is required to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private var hasNotificationPermissionGranted = false
}
