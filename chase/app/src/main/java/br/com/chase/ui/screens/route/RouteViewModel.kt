package br.com.chase.ui.screens.route

import android.Manifest
import android.app.Application
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.data.ChaseSpringRepository
import br.com.chase.data.api.RetrofitModule
import br.com.chase.data.model.RouteRequest
import br.com.chase.utils.NetworkObserver
import br.com.chase.utils.formatElapsed
import br.com.chase.utils.locationUpdatesFlow
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
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

    private var recordingJob: Job? = null
    private var lastLocation: Location? = null
    private var startTimestamp: Long = 0L

    init {
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(context).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }
        _state.value = _state.value.copy(user = FirebaseAuth.getInstance().currentUser)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION])
    fun startRecording() {
        if (_state.value.isRecording) return

        startTimestamp = currentTimeMillis()
        lastLocation = null

        _state.value = _state.value.copy(
            isRecording = true,
            isLoading = false,
            errorMessage = null,
            distance = 0.0,
            time = 0L,
            points = emptyList(),
            startLocation = null,
            endLocation = null
        )

        recordingJob = viewModelScope.launch {
            locationUpdatesFlow(
                context = context,
                intervalMs = 1500L,
                fastestIntervalMs = 800L,
                priority = Priority.PRIORITY_HIGH_ACCURACY
            ).collect { loc ->
                handleNewLocation(loc)
            }
        }
    }

    fun stopRecording() {
        recordingJob?.cancel()
        recordingJob = null
        _state.value = _state.value.copy(isRecording = false)
    }

    private var lastTimestamp = 0L
    private fun handleNewLocation(loc: Location) {
        if (!loc.hasAccuracy() || loc.accuracy > 15f) return

        val last = lastLocation
        val now = currentTimeMillis()

        if (last != null) {

            val rawDistance = last.distanceTo(loc)

            if (loc.speed < 0.5f && rawDistance < 3f) {
                return
            }

            val dt = ((now - lastTimestamp) / 1000f).coerceAtLeast(0.1f)
            val maxSpeed = 30f // m/s ≈ 108 km/h
            val maxPossibleDistance = dt * maxSpeed

            if (rawDistance > maxPossibleDistance) {
                return
            }

            _state.value = _state.value.copy(
                distance = _state.value.distance + rawDistance
            )
        }

        lastLocation = loc
        lastTimestamp = now

        val newPoint = LatLng(loc.latitude, loc.longitude)

        _state.value = _state.value.copy(
            points = _state.value.points + newPoint,
            time = now - startTimestamp,
            startLocation = _state.value.startLocation ?: newPoint,
            endLocation = newPoint
        )
    }

    fun saveRoute() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

        val st = _state.value

        val request = RouteRequest(
            uid = uid,
            name = st.name,
            description = st.description,
            startLocation = st.startLocation?.toString() ?: "Desconhecido",
            endLocation = st.endLocation?.toString() ?: "Desconhecido",
            distance = st.distance,
            recordTime = formatElapsed(st.time),
            points = st.points
        )

        _state.value = st.copy(isLoading = true, errorMessage = null)

        repo.createRoute(request)
            .onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    route = request,
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

    private fun resetRouteInfo() {
        _state.value = _state.value.copy(
            isRecording = false,
            distance = 0.0,
            time = 0L,
            points = emptyList(),
            startLocation = null,
            endLocation = null,
            name = "Sem nome",
            description = "Sem descrição"
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null, successMessage = null)
    }

    fun setError(msg: String?) {
        _state.value = _state.value.copy(errorMessage = msg)
    }
}
