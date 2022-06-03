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
    private var isTotalDistanceNull = false
    private var isTotalCaloriesBurnedNull = false
    private var isTotalSessionTimeNull = false
    private var isTotalAverageSpeedNull = false

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

        val overallStats = mainViewModel.getOverallStats()

        if (overallStats == null) {
            binding.tvOverallStatsText.visibility = View.GONE
            binding.llTotalDistance.visibility = View.GONE
            binding.llTotalCalories.visibility = View.GONE
            binding.llTotalSessionTime.visibility = View.GONE
            binding.llTotalAverageSpeed.visibility = View.GONE
        } else {
            binding.tvTotalDistance.text = overallStats.totalDistance.toString()
            binding.tvTotalCaloriesBurned.text = overallStats.totalCaloriesBurned.toString()
            binding.tvTotalSessionTime.text = overallStats.totalSessionTime.toString()
            binding.tvTotalDistance.text = overallStats.totalDistance.toString()
        }

        mainViewModel.sessionsOrderByDate.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.llRecentSessions.visibility = View.GONE
                binding.tvRecentSessionsText.visibility = View.GONE
            } else {
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