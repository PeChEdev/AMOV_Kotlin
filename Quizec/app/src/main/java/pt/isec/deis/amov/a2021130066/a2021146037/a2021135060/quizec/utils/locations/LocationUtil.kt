package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.locations

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

class LocationUtil(private val locationProvider : FusedLocationProviderClient) :
    LocationHandler {
    override var onLocation: ((Location) -> Unit)? = null
    private var locationEnabled = false
    private var locationCallback: LocationCallback? = null


    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (locationEnabled)
            return

        val notify = onLocation ?: return

        locationProvider.lastLocation
            .addOnSuccessListener { location ->
                if(location != null){
                    notify(location)
                    Log.i("Teste", "startLocationUpdates: $location")
                }
            }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).apply{
            setMinUpdateIntervalMillis(2500)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let(notify)
            }
        }
        locationCallback?.let {
                callback -> locationProvider.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ) }
        locationEnabled = true
    }

    override fun stopLocationUpdates() {
        if (!locationEnabled)
            return
        locationCallback?.let { locationProvider.removeLocationUpdates(it) }
        locationEnabled = false
    }

    fun isWithinBounds(lat1: Double, lon1: Double, lat2: Double, lon2: Double, maxDistance: Double): Boolean {
        val earthRadius = 6371000.0

        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        val deltaLat = lat2Rad - lat1Rad
        val deltaLon = lon2Rad - lon1Rad

        val x = deltaLon * Math.cos((lat1Rad + lat2Rad) / 2) * earthRadius
        val y = deltaLat * earthRadius

        val distance = Math.sqrt(x * x + y * y)

        return distance <= maxDistance
    }
}

