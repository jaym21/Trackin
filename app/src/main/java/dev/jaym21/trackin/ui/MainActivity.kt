package dev.jaym21.trackin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.databinding.ActivityMainBinding
import dev.jaym21.trackin.util.Constants

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!
    private var navHostFragment: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment


        navigateToSessionFragmentIfRequired(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToSessionFragmentIfRequired(intent)
    }
    //TODO: fix crash when notification is clicked after app is destroyed
    private fun navigateToSessionFragmentIfRequired(intent: Intent?) {
        if (navHostFragment != null) {
            if (intent?.action == Constants.ACTION_SHOW_SESSION_FRAGMENT) {
                navHostFragment!!.findNavController()
                    .navigate(R.id.action_global_sessionFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}