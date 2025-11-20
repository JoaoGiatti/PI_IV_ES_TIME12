package br.com.chase.ui.screens.route

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.data.ChaseSpringRepository
import br.com.chase.data.api.RetrofitModule
import br.com.chase.data.model.RouteRequest
import br.com.chase.utils.NetworkObserver
import br.com.chase.utils.formatElapsed
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis

class RouteViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ChaseSpringRepository(RetrofitModule.api)
    private val context = getApplication<Application>()

    private val _state = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> get() = _state

    private var lastLocation: LatLng? = null
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(context).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }
        _state.value = _state.value.copy(user = FirebaseAuth.getInstance().currentUser)
    }

    fun saveRun() = viewModelScope.launch {
        val current = _state.value.route.copy(
            uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        )

        _state.value = _state.value.copy(isLoading = true)

        repo.createRoute(current)
            .onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = "Rota salva com sucesso!"
                )
                resetRouteInfo()
            }
            .onFailure { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erro ao salvar rota"
                )
            }
    }

    fun startRun() {
        viewModelScope.launch {
            for (i in 3 downTo 1) {
                _state.value = _state.value.copy(countdown = i)
                delay(1000)
            }
            _state.value = _state.value.copy(isRecording = true, countdown = null)
            startTimer()
        }
    }

    fun stopRun() {
        _state.value = _state.value.copy(isRecording = false)
        timerJob?.cancel()
    }

    fun addLocation(location: LatLng, accuracy: Float? = null) {

        // Ignorar pontos ruins
        if (accuracy != null && accuracy > 20f) return

        // Se for o primeiro ponto
        val currentRoute = _state.value.route
        if (currentRoute.points.isEmpty()) {
            _state.value = _state.value.copy(
                route = currentRoute.copy(
                    points = listOf(location),
                )
            )
            lastLocation = location
            return
        }

        val result = FloatArray(1)
        Location.distanceBetween(
            lastLocation!!.latitude, lastLocation!!.longitude,
            location.latitude, location.longitude,
            result
        )

        val distanceMoved = result[0]

        // Ignorar ruído do GPS (parado)
        if (distanceMoved < 20f) return

        val updatedDistance = currentRoute.distance + distanceMoved

        _state.value = _state.value.copy(
            route = currentRoute.copy(
                distance = updatedDistance,
                points = currentRoute.points + location,
            )
        )

        // Atualiza último ponto válido
        lastLocation = location
    }

    private fun resetRouteInfo() {
        _state.value = _state.value.copy(
            isRecording = false,
            route = RouteRequest(
                uid = "",
                name = "Sem nome",
                description = "Sem descrição",
                startLocation = "Ponto Inicial",
                endLocation = "Ponto Final",
                distance = 0.0,
                recordTime = "00:00:00",
                points = emptyList()
            )
        )
        lastLocation = null
        timerJob?.cancel()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            val startTime = currentTimeMillis()

            while (true) {
                val elapsed = currentTimeMillis() - startTime
                val currentRoute = _state.value.route

                _state.value = _state.value.copy(
                    route = currentRoute.copy(
                        recordTime = formatElapsed(elapsed)
                    )
                )
                delay(1000)
            }
        }
    }
}