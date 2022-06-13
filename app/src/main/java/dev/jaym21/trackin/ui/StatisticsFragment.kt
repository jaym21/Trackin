package dev.jaym21.trackin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentStatisticsBinding
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.util.Utilities
import dev.jaym21.trackin.viewmodel.StatisticsViewModel

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding: FragmentStatisticsBinding
        get() = _binding!!
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private val xAxisLabelTime = ArrayList<String>()
    private val xAxisLabelSpeed = ArrayList<String>()

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

            if (it.isNullOrEmpty()) {
                binding.tvNoStatsAvailable.visibility = View.VISIBLE
                binding.tvDistanceText.visibility = View.GONE
                binding.distanceLineChart.visibility = View.GONE
                binding.tvCaloriesText.visibility = View.GONE
                binding.caloriesLineChart.visibility = View.GONE
                binding.tvTimeText.visibility = View.GONE
                binding.timeBarChart.visibility = View.GONE
                binding.tvSpeedText.visibility = View.GONE
                binding.speedBarChart.visibility = View.GONE
            } else {
                binding.tvNoStatsAvailable.visibility = View.GONE
                binding.tvDistanceText.visibility = View.VISIBLE
                binding.distanceLineChart.visibility = View.VISIBLE
                binding.tvCaloriesText.visibility = View.VISIBLE
                binding.caloriesLineChart.visibility = View.VISIBLE
                binding.tvTimeText.visibility = View.VISIBLE
                binding.timeBarChart.visibility = View.VISIBLE
                binding.tvSpeedText.visibility = View.VISIBLE
                binding.speedBarChart.visibility = View.VISIBLE

                val sessions = if (it.size > 6) {
                    it.subList(0, 6)
                } else {
                    it
                }
                val revSessions = sessions.reversed()

                setUpDistanceLineChart(revSessions)
                setUpCaloriesLineChart(revSessions)
                setUpTimeBarChart(revSessions)
                setUpSpeedBarChart(revSessions)
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpDistanceLineChart(sessions: List<Session>) {
        val entriesDistance = arrayListOf<Entry>()

        sessions.forEach {
            entriesDistance.add(Entry(it.timestamp.toFloat(), it.distanceInMeters.toFloat()))
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
        lineDataSet.color = ContextCompat.getColor(binding.root.context, R.color.red)
        lineDataSet.fillDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.chart_fade_red)

        val data = LineData(lineDataSet)

        data.setDrawValues(false)

        binding.distanceLineChart.data = data
        binding.distanceLineChart.animateXY(2000, 2000, Easing.EaseInCubic)
        binding.distanceLineChart.invalidate()
    }

    private fun setUpCaloriesLineChart(sessions: List<Session>) {
        val entriesCalories = arrayListOf<Entry>()

        sessions.forEach {
            entriesCalories.add(Entry(it.timestamp.toFloat(), it.caloriesBurned.toFloat()))
        }

        binding.caloriesLineChart.axisLeft?.isEnabled = false
        binding.caloriesLineChart.axisRight?.isEnabled = false
        binding.caloriesLineChart.xAxis?.isEnabled = false
        binding.caloriesLineChart.legend?.isEnabled = false
        binding.caloriesLineChart.description?.isEnabled = false
        binding.caloriesLineChart.setTouchEnabled(true)
        binding.caloriesLineChart.isDragEnabled = true
        binding.caloriesLineChart.setScaleEnabled(false)
        binding.caloriesLineChart.setPinchZoom(false)

        val lineDataSet = LineDataSet(entriesCalories, "Calories")

        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawFilled(true)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawIcons(false)
        lineDataSet.disableDashedLine()
        lineDataSet.color = ContextCompat.getColor(binding.root.context, R.color.orange)
        lineDataSet.fillDrawable = ContextCompat.getDrawable(binding.root.context, R.drawable.chart_fade_orange)

        val data = LineData(lineDataSet)

        data.setDrawValues(false)

        binding.caloriesLineChart.data = data
        binding.caloriesLineChart.animateXY(2000, 2000, Easing.EaseInCubic)
        binding.caloriesLineChart.invalidate()
    }

    private fun setUpTimeBarChart(sessions: List<Session>) {
        binding.timeBarChart.setDrawGridBackground(false)
        binding.timeBarChart.setDrawBarShadow(false)
        binding.timeBarChart.setDrawBorders(false)
        binding.timeBarChart.animateY(1000)
        binding.timeBarChart.setDrawValueAboveBar(true)
        binding.timeBarChart.description.isEnabled = false
        binding.timeBarChart.legend.isEnabled = false
        binding.timeBarChart.setExtraOffsets(0f, 0f, 0f, 10f)
        binding.timeBarChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        binding.timeBarChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)

        sessions.forEach {
            xAxisLabelTime.add("${Utilities.getDateFromTimestamp(it.timestamp)}'${Utilities.getMonthFullName(it.timestamp).substring(0,3)}")
        }

        val xAxis = binding.timeBarChart.xAxis
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setCenterAxisLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.labelCount = xAxisLabelTime.size
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabelTime)

        val leftAxis = binding.timeBarChart.axisLeft
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawLabels(true)
        val rightAxis = binding.timeBarChart.axisRight
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)

        val entries = ArrayList<BarEntry>()
        var i = 0
        sessions.forEach {
            entries.add(BarEntry(i.toFloat(), it.sessionTimeInMillis.toFloat()))
            i++
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.green)
        barDataSet.setDrawValues(false)
        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f
        binding.timeBarChart.data = barData
        binding.timeBarChart.invalidate()
    }

    private fun setUpSpeedBarChart(sessions: List<Session>) {
        binding.speedBarChart.setDrawGridBackground(false)
        binding.speedBarChart.setDrawBarShadow(false)
        binding.speedBarChart.setDrawBorders(false)
        binding.speedBarChart.animateY(1000)
        binding.speedBarChart.setDrawValueAboveBar(true)
        binding.speedBarChart.description.isEnabled = false
        binding.speedBarChart.legend.isEnabled = false
        binding.speedBarChart.setExtraOffsets(0f, 0f, 0f, 10f)
        binding.speedBarChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        binding.speedBarChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)

        sessions.forEach {
            xAxisLabelSpeed.add("${Utilities.getDateFromTimestamp(it.timestamp)}'${Utilities.getMonthFullName(it.timestamp).substring(0,3)}")
        }

        val xAxis = binding.speedBarChart.xAxis
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setCenterAxisLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.labelCount = xAxisLabelSpeed.size
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabelSpeed)

        val leftAxis = binding.speedBarChart.axisLeft
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawLabels(true)
        val rightAxis = binding.speedBarChart.axisRight
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)

        val entries = ArrayList<BarEntry>()
        var i = 0
        sessions.forEach {
            entries.add(BarEntry(i.toFloat(), it.avgSpeedInKMH))
            i++
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
        barDataSet.setDrawValues(false)
        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f
        binding.speedBarChart.data = barData
        binding.speedBarChart.invalidate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}