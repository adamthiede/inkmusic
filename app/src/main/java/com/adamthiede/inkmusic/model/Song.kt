package com.adamthiede.inkmusic.model

// Simple data class for a song

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val uri: String // For now, use a String to represent the song's URI
)

