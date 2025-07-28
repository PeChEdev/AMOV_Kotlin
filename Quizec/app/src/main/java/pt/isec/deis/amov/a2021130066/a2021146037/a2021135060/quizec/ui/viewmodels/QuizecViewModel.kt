package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.FAuthUtil
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.locations.LocationHandler

class QuizecViewModel(
    val locationHandler: LocationHandler
) : ViewModel() {
    var currentUser = FAuthUtil.currentUser
    var hasLocationPermission: Boolean = false

    val currentLocation = mutableStateOf(Location(null))

    init {
        locationHandler.onLocation = { location ->
            currentLocation.value = location
        }
    }

    fun startLocationUpdates() {
        if (hasLocationPermission)
            locationHandler.startLocationUpdates()
    }

    fun stopLocationUpdates() {
        locationHandler.stopLocationUpdates()
    }

    fun storeCurrentLocation() {
        if (!hasLocationPermission || currentUser == null)
            return
        currentLocation.value.let { location ->
            FAuthUtil.addMeetingPoint(location.latitude, location.longitude)
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationHandler.stopLocationUpdates()
    }

    companion object {
        fun provideFactory(locationHandler: LocationHandler) : ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return QuizecViewModel(locationHandler) as T
            }
        }
    }
}