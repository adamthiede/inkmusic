package com.adamthiede.inkmusic.ui.songs

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamthiede.inkmusic.databinding.FragmentSongsBinding
import com.adamthiede.inkmusic.model.Song
import com.adamthiede.inkmusic.ui.songs.SongsAdapter
import com.adamthiede.inkmusic.service.MusicService
import androidx.core.net.toUri

class SongsFragment : Fragment() {

    private var _binding: FragmentSongsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private var currentSongIndex: Int = -1
    private var songs: List<Song> = emptyList()

    private lateinit var songsViewModel: SongsViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                songsViewModel.loadSongs(requireContext())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        songsViewModel = ViewModelProvider(this).get(SongsViewModel::class.java)
        _binding = FragmentSongsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.songsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = SongsAdapter(songs) { position ->
            playSong(position)
        }
        recyclerView.adapter = adapter

        songsViewModel.songs.observe(viewLifecycleOwner) { songList ->
            songs = songList
            adapter.setSongs(songList)
        }

        if (checkAndRequestPermission()) {
            // Permission is granted, load songs
            songsViewModel.loadSongs(requireContext())
        }
        return root
    }

    private fun checkAndRequestPermission(): Boolean {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permission)
        }
        // return status of permission grants
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun playSong(index: Int) {
        if (index < 0 || index >= songs.size) return
        val song=songs[index]
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            action = MusicService.ACTION_START
            putExtra(MusicService.SONG_URI, song.uri)
            putExtra("SONG_TITLE", song.title)
            putExtra("SONG_ARTIST", song.artist)
            //logcat song name
            println("Playing song: ${song.title} by ${song.artist}")
        }
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}