package com.adamthiede.inkmusic.ui.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArtistsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Artists"
    }
    val text: LiveData<String> = _text
}