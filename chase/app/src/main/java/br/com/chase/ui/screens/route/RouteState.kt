package br.com.chase.ui.screens.route

import br.com.chase.data.model.RouteRequest
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser

data class RouteState(
    val isLoading: Boolean = false,
    val isRecording: Boolean = false,
    val isConnected: Boolean = true,

    val user: FirebaseUser? = null,

    val errorMessage: String? = null,
    val successMessage: String? = null,

    val name: String = "Sem nome",
    val description: String = "Sem descrição",

    val distance: Double = 0.0,
    val time: Long = 0L,

    val points: List<LatLng> = emptyList(),
    val startLocation: LatLng? = null,
    val endLocation: LatLng? = null,

    val route: RouteRequest? = null
)
