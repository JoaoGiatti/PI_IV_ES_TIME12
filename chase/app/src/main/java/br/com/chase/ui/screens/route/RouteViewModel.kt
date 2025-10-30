package br.com.chase.ui.screens.route

import android.Manifest
import android.app.Application
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.utils.locationUpdatesFlow
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import java.util.ArrayDeque

class RouteViewModel(app: Application) : AndroidViewModel(app) {

    private val fused by lazy { LocationServices.getFusedLocationProviderClient(app) }

    private val _state = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> = _state

    private var job: Job? = null
    private var lastLocation: Location? = null
    private var startMs: Long = 0L

    // janela para suavização por média móvel
    private val window = ArrayDeque<Location>()
    private val maxWindowSize = 5

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startRecording() {
        if (_state.value.isRecording) return

        lastLocation = null
        startMs = currentTimeMillis()
        _state.value = _state.value.copy(
            isRecording = true,
            isLoading = true,
            errorMessage = null,
            path = emptyList(),
            distanceMeters = 0.0,
            elapsedMs = 0L
        )

        // Warm start
        fused.lastLocation.addOnSuccessListener { it?.let(::pushLocation) }
        val cts = CancellationTokenSource()
        fused.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
            .addOnSuccessListener { it?.let(::pushLocation) }

        // Atualizações contínuas
        job = viewModelScope.launch {
            try {
                locationUpdatesFlow(
                    context = getApplication(),
                    intervalMs = 3000L,              // ⏳ intervalo maior para reduzir ruído
                    fastestIntervalMs = 1000L,
                    priority = Priority.PRIORITY_HIGH_ACCURACY
                ).collect { loc ->
                    pushLocation(loc)
                }
            } catch (se: SecurityException) {
                _state.value = _state.value.copy(
                    errorMessage = "Sem permissão de localização.",
                    isLoading = false,
                    isRecording = false
                )
            }
        }
    }

    fun stopRecording() {
        job?.cancel()
        job = null
        _state.value = _state.value.copy(isRecording = false, isLoading = false)
    }

    fun clearRoute() {
        _state.value = _state.value.copy(
            path = emptyList(),
            distanceMeters = 0.0,
            elapsedMs = 0L,
            errorMessage = null
        )
        lastLocation = null
        window.clear()
    }

    fun setError(msg: String?) {
        _state.value = _state.value.copy(errorMessage = msg)
    }

    fun setConnected(connected: Boolean) {
        _state.value = _state.value.copy(isConnected = connected)
    }

    // --- FILTRO DE LOCALIZAÇÃO ---
    private fun pushLocation(loc: Location) {
        // 1. Descartar pontos ruins
        if (loc.hasAccuracy() && loc.accuracy > 20f) return

        // 2. Filtro de deslocamento mínimo
        lastLocation?.let { if (it.distanceTo(loc) < 5f) return }

        // 3. Adicionar à janela para suavizar
        if (window.size >= maxWindowSize) window.removeFirst()
        window.addLast(loc)

        val avgLat = window.map { it.latitude }.average()
        val avgLng = window.map { it.longitude }.average()

        val smoothLoc = Location(loc).apply {
            latitude = avgLat
            longitude = avgLng
        }

        // 4. Atualiza estado com posição suavizada
        val now = currentTimeMillis()
        val prev = lastLocation
        lastLocation = smoothLoc

        _state.value = _state.value.let { s ->
            s.copy(
                isLoading = false,
                path = s.path + LatLng(smoothLoc.latitude, smoothLoc.longitude),
                distanceMeters = s.distanceMeters + (prev?.distanceTo(smoothLoc) ?: 0f),
                elapsedMs = now - startMs
            )
        }
    }
}
