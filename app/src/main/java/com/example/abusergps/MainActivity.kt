package com.example.abusergps

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    private var REQUEST_CODE_FOREGROUND = 101
    private val TAG = "Runtime Permission Demo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LocationService::class.java)
        startService(intent)
        val button1: Button = findViewById(R.id.FL)
        val button2: Button = findViewById(R.id.BL)

        button1.setOnClickListener{
            val hasLocationPermission = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if(hasLocationPermission){
                Log.i(TAG, "Fine Location Granted")
                //Requesting GPS info from location service LocationManager
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (gps_loc != null){
                    val latitude = gps_loc.latitude
                    val longitude = gps_loc.longitude
                    //Writing To a file in external storage, which can be shared with other applications
                    var data:String = "$latitude $longitude/n"
                    var dest:File = File(getExternalFilesDir(null), "location_data")
                    dest.appendText(data)
                }
            } else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_FOREGROUND)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray)
    {
        when(requestCode){
            REQUEST_CODE_FOREGROUND ->{
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Coarse Permission Request denied")
                }else{
                    Log.i(TAG, "Permission granted")
                }
            }
        }
    }
}