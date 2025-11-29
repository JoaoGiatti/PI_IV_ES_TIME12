package br.com.chase.ui.screens.route

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.data.ChaseSpringRepository
import br.com.chase.data.api.RetrofitModule
import br.com.chase.data.model.RouteRequest
import br.com.chase.utils.MalignoServerUtils
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


private const val MAX_ACCURACY_METERS = 50f
private const val MIN_MOVE_SPEED_MS = 0.25f
private const val MAX_REASONABLE_SPEED_MS = 7.5f
private const val CRAZY_JUMP_DISTANCE_METERS = 120f
private const val CRAZY_JUMP_MIN_ACCURACY_METERS = 30f
private const val BASE_MIN_MOVE_DISTANCE_METERS = 4f
private const val MAX_MIN_MOVE_DISTANCE_METERS = 12f
private const val SMOOTHING_ALPHA = 0.35f
private const val MAX_DT_SECONDS = 10f

class RouteViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ChaseSpringRepository(RetrofitModule.api)
    private val context = getApplication<Application>()

    private val _state = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> get() = _state


    private var lastRawLocation: LatLng? = null
    private var lastRawTime: Long? = null
    private var lastSmoothedLocation: LatLng? = null
    private var totalDistanceMeters: Double = 0.0
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(context).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }

        // Usuário logado
        _state.value = _state.value.copy(
            user = FirebaseAuth.getInstance().currentUser
        )
    }

    fun startRun() {
        if (_state.value.isRecording) return

        viewModelScope.launch {
            resetTrackingDataForNewRun()
            for (i in 3 downTo 1) {
                _state.value = _state.value.copy(countdown = i)
                delay(1000)
            }

            val current = _state.value
            _state.value = current.copy(
                isRecording = true,
                countdown = null,
                route = current.route.copy(
                    distance = 0.0,
                    recordTime = "00:00:00",
                    points = emptyList()
                )
            )

            startTimer()
        }
    }

    fun stopRun() {
        if (!_state.value.isRecording) return

        _state.value = _state.value.copy(isRecording = false)
        timerJob?.cancel()
    }

    fun saveRun() = viewModelScope.launch {
        val currentRoute = _state.value.route.copy(
            uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        )

        _state.value = _state.value.copy(isLoading = true)

        // Validação no servidor "do maligno"
        validateRouteOnMalignoServer(currentRoute)

        repo.createRoute(currentRoute)
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

    fun onLocationReceived(
        location: LatLng,
        accuracy: Float?,
        timeMillis: Long,
        speedMs: Float? = null
    ) {
        val currentState = _state.value
        if (!currentState.isRecording) return

        val acc = accuracy ?: Float.MAX_VALUE

        if (acc > MAX_ACCURACY_METERS) return

        val currentRoute = currentState.route
        val currentPoints = currentRoute.points

        if (currentPoints.isEmpty() || lastRawLocation == null || lastRawTime == null) {
            acceptFirstPoint(location, timeMillis)
            return
        }

        val lastRaw = lastRawLocation ?: location
        val distanceFromLastRaw = distanceBetween(lastRaw, location)
        val isCrazyJump = distanceFromLastRaw > CRAZY_JUMP_DISTANCE_METERS &&
                acc > CRAZY_JUMP_MIN_ACCURACY_METERS

        if (isCrazyJump) return

        val lastTime = lastRawTime ?: timeMillis
        val dtMillis = (timeMillis - lastTime).coerceAtLeast(1L)
        var dtSeconds = dtMillis / 1000f

        if (dtSeconds > MAX_DT_SECONDS) dtSeconds = MAX_DT_SECONDS

        val calcSpeed = if (dtSeconds > 0f) {
            distanceFromLastRaw / dtSeconds
        } else {
            0f
        }

        val baseSpeed = when {
            speedMs != null &&
                    !speedMs.isNaN() &&
                    !speedMs.isInfinite() &&
                    speedMs >= 0f -> speedMs
            else -> calcSpeed
        }

        if (baseSpeed > MAX_REASONABLE_SPEED_MS) return

        val effectiveSpeed = baseSpeed.coerceAtLeast(0f)
        val dynamicMinMoveDistance = computeDynamicMinMoveDistance(acc)
        val isMoving = distanceFromLastRaw >= dynamicMinMoveDistance &&
                (effectiveSpeed >= MIN_MOVE_SPEED_MS ||
                        distanceFromLastRaw >= dynamicMinMoveDistance * 1.8f)

        if (!isMoving) return

        totalDistanceMeters += distanceFromLastRaw
        lastRawLocation = location
        lastRawTime = timeMillis

        val smoothed = smoothForDrawing(location)
        val newPoints = currentPoints + smoothed
        val newRoute = currentRoute.copy(
            points = newPoints,
            distance = totalDistanceMeters
        )

        _state.value = currentState.copy(route = newRoute)
    }

    private fun acceptFirstPoint(location: LatLng, timeMillis: Long) {
        lastRawLocation = location
        lastRawTime = timeMillis
        lastSmoothedLocation = location
        totalDistanceMeters = 0.0

        val currentState = _state.value
        val currentRoute = currentState.route

        val newRoute = currentRoute.copy(
            points = listOf(location),
            distance = 0.0
        )

        _state.value = currentState.copy(route = newRoute)
    }

    private fun distanceBetween(a: LatLng, b: LatLng): Float {
        val result = FloatArray(1)
        Location.distanceBetween(
            a.latitude, a.longitude,
            b.latitude, b.longitude,
            result
        )
        return result[0]
    }

    private fun smoothForDrawing(raw: LatLng): LatLng {
        val prev = lastSmoothedLocation
        if (prev == null) {
            lastSmoothedLocation = raw
            return raw
        }

        val lat = prev.latitude + (raw.latitude - prev.latitude) * SMOOTHING_ALPHA
        val lng = prev.longitude + (raw.longitude - prev.longitude) * SMOOTHING_ALPHA

        val smoothed = LatLng(lat, lng)
        lastSmoothedLocation = smoothed
        return smoothed
    }

    private fun computeDynamicMinMoveDistance(accuracy: Float): Float {
        val basedOnAccuracy = accuracy * 0.35f
        return basedOnAccuracy
            .coerceAtLeast(BASE_MIN_MOVE_DISTANCE_METERS)
            .coerceAtMost(MAX_MIN_MOVE_DISTANCE_METERS)
    }

    private fun resetTrackingDataForNewRun() {
        lastRawLocation = null
        lastRawTime = null
        lastSmoothedLocation = null
        totalDistanceMeters = 0.0
        timerJob?.cancel()
    }

    private fun resetRouteInfo() {
        resetTrackingDataForNewRun()

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
    }

    private fun startTimer() {
        timerJob?.cancel()

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

    private fun validateRouteOnMalignoServer(routeRequest: RouteRequest) {
        val client = MalignoServerUtils { valido ->
            _state.value = _state.value.copy(validacaoRota = valido)
        }

        client.enviarPedido(routeRequest)
    }
}
