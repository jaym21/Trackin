package dev.jaym21.trackin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.adapter.ISessionRVAdapter
import dev.jaym21.trackin.adapter.SessionRVAdapter
import dev.jaym21.trackin.databinding.FragmentHomeBinding
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.viewmodel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), ISessionRVAdapter {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    @set:Inject
    var userName = ""
    private val sessionAdapter = SessionRVAdapter(this)

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

        setUpRecyclerView()

        binding.tvGreetUser.text = "Hello, $userName"

        mainViewModel.totalDistance.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.tvOverallStatsText.visibility = View.GONE
                binding.llTotalDistance.visibility = View.GONE
            } else {
                binding.tvOverallStatsText.visibility = View.VISIBLE
                binding.llTotalDistance.visibility = View.VISIBLE
                binding.tvTotalDistance.text = it.toString()
            }
        }

        mainViewModel.totalCaloriesBurned.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.llTotalCaloriesBurned.visibility = View.GONE
            } else {
                binding.llTotalCaloriesBurned.visibility = View.VISIBLE
                binding.tvTotalCaloriesBurned.text = it.toString()
            }
        }

        mainViewModel.totalSessionTime.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.llTotalSessionTime.visibility = View.GONE
            } else {
                binding.llTotalSessionTime.visibility = View.VISIBLE
                binding.tvTotalSessionTime.text = it.toString()
            }
        }

        mainViewModel.totalAverageSpeed.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.llTotalAverageSpeed.visibility = View.GONE
            } else {
                binding.llTotalAverageSpeed.visibility = View.VISIBLE
                binding.tvTotalAverageSpeed.text = it.toString()
            }
        }

        mainViewModel.sessionsOrderByDate.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.llRecentSessions.visibility = View.GONE
                binding.tvRecentSessionsText.visibility = View.GONE
            } else {
                binding.llRecentSessions.visibility = View.VISIBLE
                binding.tvRecentSessionsText.visibility = View.VISIBLE
                val recentSessions = if (it.size > 10) {
                    it.subList(0, 10)
                } else {
                    it
                }

                sessionAdapter.submitList(recentSessions)
            }
        }

        binding.fabNewRun.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sessionFragment)
        }

        binding.llAllSessions.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSessionsFragment)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvRecentSessions.apply {
            adapter = sessionAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onSessionClick(session: Session) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}