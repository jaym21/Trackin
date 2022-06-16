package dev.jaym21.trackin.ui.onboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentDailyGoalBinding
import dev.jaym21.trackin.ui.MainActivity
import dev.jaym21.trackin.util.Constants
import dev.jaym21.trackin.util.Utilities
import dev.jaym21.trackin.worker.DailyGoalWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class DailyGoalFragment : Fragment() {

    private var _binding: FragmentDailyGoalBinding? = null
    private val binding: FragmentDailyGoalBinding
        get() = _binding!!
    private val goalDistance = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
    private val goalCalories = listOf(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDailyGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val distanceAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, goalDistance)
        distanceAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        binding.spinnerDistance.adapter = distanceAdapter

        val caloriesAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, goalCalories)
        caloriesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        binding.spinnerCalories.adapter = caloriesAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivSubmit.setOnClickListener {
            sharedPreferences.edit()
                .putInt(Constants.CALORIES_GOAL, binding.spinnerCalories.selectedItem as Int)
                .putInt(Constants.DISTANCE_GOAL, binding.spinnerDistance.selectedItem as Int)
                .putBoolean(Constants.IS_FIRST_RUN, false)
                .apply()

            //enqueuing worker
            val delay = Utilities.getNextDayTimestamp() - System.currentTimeMillis()
            if (delay > 0) {
                val dailyWork = OneTimeWorkRequest.Builder(DailyGoalWorker::class.java)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .build()

                WorkManager.getInstance(requireContext().applicationContext).enqueue(dailyWork)
            }

            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}