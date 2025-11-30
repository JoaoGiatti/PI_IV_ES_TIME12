package br.com.chase.ui.screens.route

import br.com.chase.data.model.RouteRequest
import br.com.chase.data.model.RouteResponse
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser

enum class RunMode {
    RECORD, // gravar treino normal
    COMPETE // competir em uma rota / prova
}


data class RouteState(
    val isLoading: Boolean = false,
    val isRecording: Boolean = false,
    val isConnected: Boolean = true,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    val mode: RunMode = RunMode.RECORD,
    val competitionRoute: RouteResponse? = null,
    val competitionPoints: List<LatLng> = emptyList(),
    val competitionProgress: Float = 0f,
    val isCompetitionPathValid: Boolean? = null,

    val route: RouteRequest = RouteRequest(
        uid = "",
        name = "Sem nome",
        description = "Sem descrição",
        startLocation = "Ponto Inicial",
        endLocation = "Ponto Final",
        distance = 0.0,
        recordTime = "00:00:00",
        points = emptyList()
    ),
    val validacaoRota: Boolean = false,

    val countdown: Int? = null
)