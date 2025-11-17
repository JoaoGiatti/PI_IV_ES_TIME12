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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.System.currentTimeMillis
import java.util.ArrayDeque

class RouteViewModel(app: Application) : AndroidViewModel(app) {

    private val chaseSpringRepository = ChaseSpringRepository(RetrofitModule.api)
    private val appContext = getApplication<Application>().applicationContext

    private val fused by lazy { LocationServices.getFusedLocationProviderClient(app) }

    private val _state = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> = _state

    init {
        // 👇 Começa a observar a conexão assim que o ViewModel é criado
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }

        currentUser()
    }

    fun saveRoute() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

        val routeRequest = RouteRequest(
            uid = uid,
            name = _state.value.name,
            description = _state.value.description,
            startLocation = "Endereço desconhecido",
            endLocation = "Endereço desconhecido",
            distance = _state.value.distance,
            recordTime = formatElapsed(_state.value.time),
            points = _state.value.points
        )

        _state.value = _state.value.copy(route = routeRequest, isLoading = true, errorMessage = null)

        chaseSpringRepository.createRoute(routeRequest)
            .onSuccess {
                _state.value = _state.value.copy(isLoading = false)
            }
            .onFailure { e ->
                _state.value = _state.value.copy(errorMessage = e.message, isLoading = false)
            }
    }

    fun currentUser() = viewModelScope.launch {
        _state.value = _state.value.copy(user = FirebaseAuth.getInstance().currentUser)
    }

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
            points = emptyList(),
            distance = 0.0,
            time = 0L
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
                    intervalMs = 3000L,
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
        clearRouteInfo()
    }

    fun clearRouteInfo() {
        _state.value = _state.value.copy(
            isLoading = false,
            isRecording = false,
            errorMessage = null,

            route = null,

            name = "Sem nome",
            description = "Sem descrição",

            distance = 0.0,
            time = 0L,

            points = emptyList(),
            startLocation = null,
            endLocation = null,
        )
    }

    // ---------------------------------------------------------------------------------------------

    fun setError(msg: String?) {
        _state.value = _state.value.copy(errorMessage = msg)
    }

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
                points = s.points + LatLng(smoothLoc.latitude, smoothLoc.longitude),
                distance = s.distance + (prev?.distanceTo(smoothLoc) ?: 0f),
                time = now - startMs
            )
        }
    }
}
