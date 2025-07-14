package com.adamthiede.inkmusic.ui.songs

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamthiede.inkmusic.model.Song

class SongsViewModel : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>().apply {
        value = emptyList()
    }
    val songs: LiveData<List<Song>> = _songs

    fun loadSongs(context: Context) {
        val songList = mutableListOf<Song>()
        val collection = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DISPLAY_NAME
        )
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor = context.contentResolver.query(
            collection,
            projection,
            null, // No selection: get all audio files
            null,
            sortOrder
        )
        if (cursor == null) {
            _songs.value = emptyList()
            return
        }
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val title = cursor.getString(titleColumn)
            val artist = cursor.getString(artistColumn)
            val displayName = cursor.getString(displayNameColumn)
            val uri = Uri.withAppendedPath(collection, id.toString()).toString()
            // Use displayName if title is missing
            songList.add(Song(id, title ?: displayName ?: "Unknown", artist ?: "Unknown", uri))
        }
        cursor.close()
        _songs.value = songList
    }

}