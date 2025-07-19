// File: app/src/main/java/com/adamthiede/inkmusic/ui/nowplaying/NowPlayingFragment.kt
package com.adamthiede.inkmusic.ui.nowplaying

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adamthiede.inkmusic.databinding.FragmentNowplayingBinding
import com.adamthiede.inkmusic.service.MusicService

class NowPlayingFragment : Fragment() {

    private var _binding: FragmentNowplayingBinding? = null
    private val binding get() = _binding!!
    private var isPlaying = false

    private val nowPlayingReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: android.content.Context?, intent: Intent?) {
            val title = intent?.getStringExtra("SONG_TITLE") ?: "Unknown Song"
            val artist = intent?.getStringExtra("SONG_ARTIST") ?: "Unknown Artist"
            //logcat song name
            println("NOW PLAYING RECEIVER: Received update for now playing song")
            println("Title: $title, Artist: $artist")
            binding.songTitle.text = title
            binding.artistName.text = artist
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNowplayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        ContextCompat.registerReceiver(
            requireContext(),
            nowPlayingReceiver,
            IntentFilter(MusicService.UPDATE_NOW_PLAYING),
            ContextCompat.RECEIVER_EXPORTED
        )

        //logcat
        println("NowPlayingFragment: onViewCreated called, registering receiver for now playing updates")


        val queryIntent = Intent(MusicService.QUERY_NOW_PLAYING)
        requireContext().sendBroadcast(queryIntent)

        binding.playpausebutton.setOnClickListener {
            MusicService.QUERY_NOW_PLAYING
            MusicService.UPDATE_NOW_PLAYING
            val action = if (isPlaying) MusicService.ACTION_PAUSE else MusicService.ACTION_PLAY
            val intent = Intent(requireContext(), MusicService::class.java).apply { this.action = action }
            requireContext().startService(intent)
            isPlaying = !isPlaying
            binding.playpausebutton.text = if (isPlaying) "Pause" else "Play"
        }
        binding.previousbutton.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply { action = MusicService.ACTION_PREV }
            requireContext().startService(intent)
        }
        binding.nextbutton.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply { action = MusicService.ACTION_NEXT }
            requireContext().startService(intent)
        }
        binding.stopbutton.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply { action = MusicService.ACTION_STOP }
            requireContext().startService(intent)
            isPlaying = false
            binding.playpausebutton.text = "Play"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(nowPlayingReceiver)
        _binding = null
    }
}