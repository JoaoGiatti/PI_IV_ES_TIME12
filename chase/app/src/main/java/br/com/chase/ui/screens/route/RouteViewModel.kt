package br.com.chase.ui.screens.route

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RouteViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _state = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> = _state

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        viewModelScope.launch {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    _state.value = _state.value.copy(
                        userLocation = LatLng(loc.latitude, loc.longitude),
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        errorMessage = "Não foi possível obter localização",
                        isLoading = false
                    )
                }
            }
        }
    }
}
