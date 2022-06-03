package dev.jaym21.trackin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentHomeBinding
import dev.jaym21.trackin.viewmodel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    @set:Inject
    var userName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvGreetUser.text = "Hello, $userName"

        mainViewModel.

        binding.fabNewRun.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sessionFragment)
        }

        binding.llAllSessions.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSessionsFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}