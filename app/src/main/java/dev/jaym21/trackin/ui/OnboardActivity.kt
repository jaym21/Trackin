package dev.jaym21.trackin.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.databinding.ActivityOnboardBinding
import dev.jaym21.trackin.util.Constants
import javax.inject.Inject

@AndroidEntryPoint
class OnboardActivity : AppCompatActivity() {

    private var _binding: ActivityOnboardBinding? = null
    private val binding: ActivityOnboardBinding
        get() = _binding!!
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSubmit.setOnClickListener {
            if (binding.etUserName.text.toString().isNotEmpty()) {
                if (binding.etUserWeight.text.toString().isNotEmpty()) {

                    sharedPreferences.edit().putString(Constants.USER_NAME, binding.etUserName.text.toString())
                        .putFloat(Constants.USER_WEIGHT, binding.etUserWeight.text.toString().toFloat())
                        .putBoolean(Constants.IS_FIRST_RUN, false)
                        .apply()


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