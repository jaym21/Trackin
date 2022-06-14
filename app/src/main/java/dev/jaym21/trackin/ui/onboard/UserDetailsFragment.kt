package dev.jaym21.trackin.ui.onboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.FragmentUserDetailsBinding
import dev.jaym21.trackin.ui.MainActivity
import dev.jaym21.trackin.util.Constants
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding: FragmentUserDetailsBinding
        get() = _binding!!
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivNext.setOnClickListener {
            if (binding.etUserName.text.toString().isNotEmpty()) {
                if (binding.etUserWeight.text.toString().isNotEmpty()) {
                    sharedPreferences.edit()
                        .putString(Constants.USER_NAME, binding.etUserName.text.toString())
                        .putFloat(Constants.USER_WEIGHT, binding.etUserWeight.text.toString().toFloat())
                        .apply()

                    findNavController().navigate(R.id.action_userDetailsFragment_to_dailyGoalFragment)
                } else {
                    Snackbar.make(binding.root, "Your weight is needed to proceed", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(binding.root, "Your name is needed to proceed", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}