package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.locations

import android.location.Location

interface LocationHandler {
    var onLocation : ((Location) -> Unit)?
    fun startLocationUpdates()
    fun stopLocationUpdates()
}