package dev.jaym21.trackin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentStatisticsBinding
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.viewmodel.StatisticsViewModel

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding: FragmentStatisticsBinding
        get() = _binding!!
    private val statisticsViewModel: StatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statisticsViewModel.sessionsOrderByDate.observe(viewLifecycleOwner) {
            val sessions = if (it.size > 6) {
                it.subList(0, 6)
            } else {
                it
            }
            val revSessions = sessions.reversed()
            setUpDistanceLineChart(revSessions)
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpDistanceLineChart(sessions: List<Session>) {

        val entriesDistance = arrayListOf<Entry>()

        sessions.forEach {
            entriesDistance.add(Entry(it.sessionTimeInMillis.toFloat(), it.distanceInMeters.toFloat()))
        }

        binding.distanceLineChart.axisLeft?.isEnabled = false
        binding.distanceLineChart.axisRight?.isEnabled = false
        binding.distanceLineChart.xAxis?.isEnabled = false
        binding.distanceLineChart.legend?.isEnabled = false
        binding.distanceLineChart.description?.isEnabled = false
        binding.distanceLineChart.setTouchEnabled(true)
        binding.distanceLineChart.isDragEnabled = true
        binding.distanceLineChart.setScaleEnabled(false)
        binding.distanceLineChart.setPinchZoom(false)

        val lineDataSet = LineDataSet(entriesDistance, "Distance")

        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawFilled(true)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawIcons(false)
        lineDataSet.disableDashedLine()

        val data = LineData(lineDataSet)

        data.setDrawValues(false)

        binding.distanceLineChart.data = data
        binding.distanceLineChart.animateXY(2000, 2000, Easing.EaseInCubic)
        binding.distanceLineChart.invalidate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}