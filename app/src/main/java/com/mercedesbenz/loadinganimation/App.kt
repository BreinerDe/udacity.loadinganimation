package com.mercedesbenz.loadinganimation

import android.app.Application
import android.content.Context


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: Context? = null
            private set
    }
}