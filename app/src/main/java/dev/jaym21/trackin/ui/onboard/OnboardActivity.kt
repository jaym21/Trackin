package dev.jaym21.trackin.ui.onboard

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.databinding.ActivityOnboardBinding
import dev.jaym21.trackin.ui.MainActivity
import dev.jaym21.trackin.util.Constants
import javax.inject.Inject

@AndroidEntryPoint
class OnboardActivity : AppCompatActivity() {

    private var _binding: ActivityOnboardBinding? = null
    private val binding: ActivityOnboardBinding
        get() = _binding!!
    @set:Inject
    var isFirstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isFirstRun) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}