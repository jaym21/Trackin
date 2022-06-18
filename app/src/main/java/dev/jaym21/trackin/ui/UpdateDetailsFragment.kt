package dev.jaym21.trackin.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentUpdateDetailsBinding
import dev.jaym21.trackin.util.Constants
import javax.inject.Inject

@AndroidEntryPoint
class UpdateDetailsFragment : Fragment() {

    private var _binding: FragmentUpdateDetailsBinding? = null
    private val binding: FragmentUpdateDetailsBinding
        get() = _binding!!
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @set:Inject
    var userName = ""
    @set:Inject
    var userWeight = 70f
    private val goalDistance = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
    private val goalCalories = listOf(100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateDetailsBinding.inflate(inflater, container, false)
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

        binding.etUserName.setText(userName)
        binding.etUserWeight.setText(userWeight.toString())

        val distanceIndex = goalDistance.indexOf(sharedPreferences.getInt(Constants.DISTANCE_GOAL, 1))
        val caloriesIndex = goalCalories.indexOf(sharedPreferences.getInt(Constants.CALORIES_GOAL, 100))

        Log.d("TAGYOYO", "onViewCreated: distanceIndex $distanceIndex caloriesIndex $caloriesIndex")

        binding.spinnerDistance.setSelection(distanceIndex)
        binding.spinnerCalories.setSelection(caloriesIndex)

        binding.ivUpdate.setOnClickListener {
            if (binding.etUserName.text.toString().isNotEmpty()) {
                if (binding.etUserWeight.text.toString().isNotEmpty()) {
                    sharedPreferences.edit()
                        .putString(Constants.USER_NAME, binding.etUserName.text.toString())
                        .putFloat(Constants.USER_WEIGHT, binding.etUserWeight.text.toString().toFloat())
                        .putInt(Constants.DISTANCE_GOAL, binding.spinnerDistance.selectedItem as Int)
                        .putInt(Constants.CALORIES_GOAL, binding.spinnerCalories.selectedItem as Int)
                        .apply()

                    findNavController().popBackStack()
                } else {
                    Snackbar.make(binding.root, "Your weight is needed to proceed", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.root, "Your name is needed to proceed", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}