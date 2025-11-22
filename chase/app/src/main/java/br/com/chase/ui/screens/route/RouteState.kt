package br.com.chase.ui.screens.route

import br.com.chase.data.model.RouteRequest
import com.google.firebase.auth.FirebaseUser

data class RouteState(
    val isLoading: Boolean = false,
    val isRecording: Boolean = false,
    val isConnected: Boolean = true,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,

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
