package dev.gbautin.blablance.ui.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JournalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Journal Fragment"
    }
    val text: LiveData<String> = _text
}