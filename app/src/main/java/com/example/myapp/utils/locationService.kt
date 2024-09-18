package com.example.myapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine

class locationService {
    suspend fun getLocation(context: Context): Location?{
        val fusedLocation = LocationServices.getFusedLocationProviderClient(context)
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ||  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        fun permissions():Boolean{
            return  ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED

        }
        if(!gpsEnable || permissions()){
            return null
        }

        return suspendCancellableCoroutine {  con ->

            fusedLocation.lastLocation.apply {
                if(isComplete){
                    if(isSuccessful){
                        con.resume(result){}
                    } else {
                        con.resume(null){  }
                    }
                }
                addOnSuccessListener {
                    con.resume(it){}
                }
                 addOnFailureListener{
                     con.resume(null){}
                 }
                addOnCanceledListener {
                    con.resume(null){}
                }



            }
        }
    }
}
