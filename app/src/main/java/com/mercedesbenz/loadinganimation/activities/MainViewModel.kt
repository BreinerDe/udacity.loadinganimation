package com.mercedesbenz.loadinganimation.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mercedesbenz.loadinganimation.messaging.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val isDownloadCompleted: LiveData<Boolean?>
        get() = Repository.repIsDownloadCompleted

    private val _checked = MutableLiveData<Int?>()
    val checked: LiveData<Int?>
        get() = _checked

    init {
        _checked.value = -1
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass == MainViewModel::class.java) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
