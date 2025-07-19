package com.adamthiede.inkmusic.service
// File: app/src/main/java/com/adamthiede/inkmusic/service/MusicService.kt
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.net.toUri

class MusicService : Service() {
    companion object {
        const val ACTION_PLAY = "com.adamthiede.inkmusic.PLAY"
        const val ACTION_PAUSE = "com.adamthiede.inkmusic.PAUSE"
        const val ACTION_STOP = "com.adamthiede.inkmusic.STOP"
        const val ACTION_NEXT = "com.adamthiede.inkmusic.NEXT"
        const val ACTION_PREV = "com.adamthiede.inkmusic.PREV"
        const val ACTION_START = "com.adamthiede.inkmusic.START"
        const val SONG_URI = "SONG_URI"
    }
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val songUri = intent.getStringExtra(SONG_URI)?.toUri()
                if (songUri != null) {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this, songUri)
                    mediaPlayer?.setOnCompletionListener { stopSelf() }
                    mediaPlayer?.start()
                }
            }
            ACTION_PLAY -> mediaPlayer?.start()
            ACTION_PAUSE -> mediaPlayer?.pause()
            ACTION_STOP -> {
                mediaPlayer?.stop()
                stopSelf()
            }
            // Implement NEXT/PREV as needed
        }
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}