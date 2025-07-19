package com.adamthiede.inkmusic.ui.nowplaying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.adamthiede.inkmusic.databinding.FragmentNowplayingBinding
import com.adamthiede.inkmusic.service.MusicService
import android.content.Intent

class NowPlayingFragment : Fragment() {

    private var _binding: FragmentNowplayingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        _binding = FragmentNowplayingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.playPause.setOnClickListener {
            val action = MusicService.ACTION_PLAY // or toggle with PAUSE
            val intent = Intent(requireContext(), MusicService::class.java).apply { this.action = action }
            requireContext().startService(intent)
        }
        binding.previous.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply { action = MusicService.ACTION_PREV }
            requireContext().startService(intent)
        }
        binding.next.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply { action = MusicService.ACTION_NEXT }
            requireContext().startService(intent)
        }
        binding.root.findViewById<View>(android.R.id.button1)?.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply { action = MusicService.ACTION_STOP }
            requireContext().startService(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}