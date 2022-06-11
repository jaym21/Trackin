package dev.jaym21.trackin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentSessionDetailsBinding
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.util.Constants
import dev.jaym21.trackin.util.Utilities
import kotlin.math.round

class SessionDetailsFragment : Fragment() {

    private var _binding: FragmentSessionDetailsBinding? = null
    private val binding: FragmentSessionDetailsBinding
        get() = _binding!!
    private var selectedSession: Session? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSessionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedSession = arguments?.getParcelable(Constants.KEY_SESSION)

        if (selectedSession != null) {

            Glide.with(binding.root).load(selectedSession?.sessionImage).into(binding.ivSessionImage)

            binding.tvTotalSessionTime.text = Utilities.timeToTimerFormat(selectedSession?.sessionTimeInMillis!!)

            val km = selectedSession?.distanceInMeters!! / 1000f
            val totalDistance = round(km * 10f) / 10f
            binding.tvTotalSessionDistance.text = totalDistance.toString()

            binding.tvTotalSessionCalories.text = selectedSession?.caloriesBurned.toString()
            binding.tvTotalSessionAverageSpeed.text = selectedSession?.avgSpeedInKMH.toString()

            binding.ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}