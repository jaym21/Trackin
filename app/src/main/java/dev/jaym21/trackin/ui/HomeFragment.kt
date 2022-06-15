package dev.jaym21.trackin.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.adapter.ISessionRVAdapter
import dev.jaym21.trackin.adapter.SessionRVAdapter
import dev.jaym21.trackin.databinding.FragmentHomeBinding
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.util.Constants
import dev.jaym21.trackin.util.Utilities
import dev.jaym21.trackin.viewmodel.MainViewModel
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class HomeFragment : Fragment(), ISessionRVAdapter {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @set:Inject
    var userName = ""
    @set:Inject
    var distanceGoal = 0
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

        setUpGoalPieChart()

        mainViewModel.totalDistance.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.tvOverallStatsText.visibility = View.GONE
                binding.llMoreStats.visibility = View.GONE
                binding.llTotalDistance.visibility = View.GONE
            } else {
                binding.tvOverallStatsText.visibility = View.VISIBLE
                binding.llMoreStats.visibility = View.VISIBLE
                binding.llTotalDistance.visibility = View.VISIBLE

                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f

                binding.tvTotalDistance.text = totalDistance.toString()

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
                binding.tvTotalSessionTime.text = Utilities.timeToOverallStatsFormat(it)
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

        binding.llMoreStats.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_statisticsFragment)
        }

        binding.ivNewSession.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sessionFragment)
        }

        binding.llAllSessions.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSessionsFragment)
        }
    }

    private fun setUpGoalPieChart() {

        val distanceGoalCompleted = sharedPreferences.getFloat(Constants.DISTANCE_GOAL_COMPLETED, 0F)
        binding.tvDailyDistanceGoalCompletion.text = "${distanceGoalCompleted}/${distanceGoal} km"

        binding.pieChartGoal.isDrawHoleEnabled = true
        binding.pieChartGoal.holeRadius = 65f
        binding.pieChartGoal.setUsePercentValues(true)
        binding.pieChartGoal.setDrawEntryLabels(false)
        binding.pieChartGoal.setDrawCenterText(true)
        binding.pieChartGoal.isRotationEnabled = false
        binding.pieChartGoal.setTouchEnabled(false)
        binding.pieChartGoal.highlightValues(null)
        binding.pieChartGoal.setHoleColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.pieChartGoal.animateY(1400, Easing.EaseInOutQuad)

        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        entries.add(PieEntry(distanceGoalCompleted, "Distance Completed"))
        colors.add(ContextCompat.getColor(requireContext(), R.color.red))
        entries.add(PieEntry(distanceGoal - distanceGoalCompleted, "Distance Remaining"))
        colors.add(ContextCompat.getColor(requireContext(), R.color.red_light))

        val dataSet = PieDataSet(entries, "Distance Goal")
        dataSet.colors = colors
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.transparent)

        val data = PieData(dataSet)
        binding.pieChartGoal.legend.isEnabled = false
        binding.pieChartGoal.data = data
        binding.pieChartGoal.invalidate()
    }

    private fun setUpRecyclerView() {
        binding.rvRecentSessions.apply {
            adapter = sessionAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onSessionClick(session: Session) {
        val bundle = Bundle().apply {
            putParcelable(Constants.KEY_SESSION, session)
        }
        findNavController().navigate(R.id.action_homeFragment_to_sessionDetailsFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}