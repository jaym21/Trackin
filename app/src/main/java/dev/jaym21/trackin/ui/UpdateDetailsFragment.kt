package dev.jaym21.trackin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentUpdateDetailsBinding

class UpdateDetailsFragment : Fragment() {

    private var _binding: FragmentUpdateDetailsBinding? = null
    private val binding: FragmentUpdateDetailsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}