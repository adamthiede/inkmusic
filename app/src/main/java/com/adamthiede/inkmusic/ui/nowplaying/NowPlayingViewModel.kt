package com.adamthiede.inkmusic.ui.nowplaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NowPlayingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Now Playing"
    }
    val text: LiveData<String> = _text
}