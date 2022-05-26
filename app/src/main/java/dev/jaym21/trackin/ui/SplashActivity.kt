package dev.jaym21.trackin.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.jaym21.trackin.R
import dev.jaym21.trackin.util.Constants

class SplashActivity: AppCompatActivity() {

    private val PERMISSIONS_BELOW_Q = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val PERMISSIONS_ABOVE_Q = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkIfPermissionsGranted()
    }

    override fun onResume() {
        super.onResume()

        if (checkIfPermissionsGranted()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                allPermissionsLauncher.launch(PERMISSIONS_ABOVE_Q)
            } else {
                allPermissionsLauncher.launch(PERMISSIONS_BELOW_Q)
            }
        }
    }

    private val allPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
        if (perms.values.all { it }) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val deniedPermission = perms.filter { !it.value }.keys.first()
            if (!shouldShowRequestPermissionRationale(deniedPermission)) {
                showSettingsDialog()
            }
        }
    }

    private fun checkIfPermissionsGranted(): Boolean {
        var isGranted = false

        isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            hasPermissions(this, *PERMISSIONS_ABOVE_Q)
        } else {
            hasPermissions(this, *PERMISSIONS_BELOW_Q)
        }

        return isGranted
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this, R.style.PermissionAlertDialog).create()

        val view = layoutInflater.inflate(R.layout.permission_settings_layout, null)
        val settings: TextView = view.findViewById(R.id.tvSettings)

        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)

        settings.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
            builder.dismiss()
        }
        builder.show()
    }
}