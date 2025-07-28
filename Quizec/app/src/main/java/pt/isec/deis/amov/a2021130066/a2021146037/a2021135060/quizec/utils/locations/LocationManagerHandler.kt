package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.locations

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.location.LocationListenerCompat

class LocationManagerHandler(private val locationManager: LocationManager) : LocationHandler {
    override var onLocation: ((Location) -> Unit)? = null
    private var locationEnabled = false
    private var locationListener : LocationListenerCompat? = null

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (locationEnabled)
            return

        val notify = onLocation ?: return
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            notify(it)
        }


        locationListener = LocationListenerCompat { location ->
            notify(location)
        }

        locationListener?.let {
                listener ->  locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            10f,
            listener,
            Looper.getMainLooper()
        ) }

        locationEnabled = true
    }

    override fun stopLocationUpdates() {
        if (!locationEnabled)
            return
        locationListener?.let { locationManager.removeUpdates(it) }
        locationEnabled = false
    }
}