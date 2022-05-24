package dev.jaym21.trackin.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.jaym21.trackin.util.Constants

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFineLocPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val isCoarseLocPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val isBackgroundLocPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (isFineLocPermissionGranted) {
                if (isCoarseLocPermissionGranted) {
                    if (isBackgroundLocPermissionGranted) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), Constants.ACCESS_FINE_LOCATION_REQUEST_CODE)
                    }
                } else {
                    ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), Constants.ACCESS_COARSE_LOCATION_REQUEST_CODE)
                }
            } else {
                ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Constants.ACCESS_BACKGROUND_LOCATION_REQUEST_CODE)
            }

        } else {
            if (isFineLocPermissionGranted) {
                if (isCoarseLocPermissionGranted) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), Constants.ACCESS_FINE_LOCATION_REQUEST_CODE)
                }
            } else {
                ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Constants.ACCESS_COARSE_LOCATION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            Constants.ACCESS_FINE_LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                    val intent = Intent(this, NoPermissionActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            Constants.ACCESS_COARSE_LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                    val intent = Intent(this, NoPermissionActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            Constants.ACCESS_BACKGROUND_LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                    val intent = Intent(this, NoPermissionActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}