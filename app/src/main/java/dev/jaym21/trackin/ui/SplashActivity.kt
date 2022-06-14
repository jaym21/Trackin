package dev.jaym21.trackin.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.jaym21.trackin.ui.onboard.OnboardActivity
import dev.jaym21.trackin.util.Constants
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class SplashActivity: AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val PERMISSIONS_BELOW_Q = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val PERMISSIONS_ABOVE_Q = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    )
    @set:Inject
    var isFirstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkIfPermissionsGranted()) {
            if (isFirstRun) {
                val onboardIntent = Intent(this, OnboardActivity::class.java)
                startActivity(onboardIntent)
                finish()
            } else {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun checkIfPermissionsGranted(): Boolean =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(this,*PERMISSIONS_BELOW_Q)
        } else {
            EasyPermissions.hasPermissions(this,*PERMISSIONS_ABOVE_Q)
        }

    private fun requestLocationPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "This app requires location permissions to function, allow all location permissions.",
                Constants.LOCATION_REQUEST_CODE,
                *PERMISSIONS_BELOW_Q
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app requires location permissions to function, allow all location permissions.",
                Constants.LOCATION_REQUEST_CODE,
                *PERMISSIONS_ABOVE_Q
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}
}