package com.mr2.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedTagViewModel (application: Application) : AndroidViewModel(application)  {
    private val mutableSelectedItem = MutableLiveData<String>()
    var selectedItem: LiveData<String> = mutableSelectedItem

    fun selectItem(item: String) {
        mutableSelectedItem.value = item
    }
}