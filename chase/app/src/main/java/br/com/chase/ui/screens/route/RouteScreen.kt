package br.com.chase.ui.screens.route

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(vm: RouteViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    val ctx = LocalContext.current

    // ---- permissões (fine/coarse) ----
    val hasFine = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    val hasCoarse = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    var hasLocationPermission by remember { mutableStateOf(hasFine || hasCoarse) }
    val isPrecise by remember { derivedStateOf { hasFine } }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val requestPermissions = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        val fine = granted[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = granted[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = fine || coarse
        vm.setError(
            when {
                !hasLocationPermission -> "Permissão de localização negada."
                !fine -> "Permissão concedida como aproximada. Ative 'Precisão exata' para melhor resultado."
                else -> null
            }
        )
        if (hasLocationPermission) {
            //aaaaaaaaa
        }
    }

    // ---- settings (GPS/Wi-Fi) ----
    val settingsClient = remember { LocationServices.getSettingsClient(ctx) }
    val locationRequest = remember(isPrecise) {
        LocationRequest.Builder(1000L)
            .setMinUpdateIntervalMillis(500L)
            .setPriority(if (isPrecise) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()
    }
    val settingsRequest = remember(locationRequest) {
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
    }

    val resolutionLauncher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            vm.startRecording()
        } else {
            vm.setError("Localização desativada pelo sistema.")
        }
    }

    fun ensureLocationThenStart(
        isPrecise: Boolean,
        onOk: () -> Unit,
        onError: (String) -> Unit
    ) {
        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { ex ->
                if (ex is ResolvableApiException) {
                    resolutionLauncher.launch(IntentSenderRequest.Builder(ex.resolution).build())
                } else {
                    onError("Ative a Localização (GPS/Wi-Fi) nas configurações do sistema.")
                }
            }
    }

    // ---- câmera do mapa ----
    val cameraState = rememberCameraPositionState()

    // Ajusta a câmera quando o path muda
    LaunchedEffect(state.path) {
        val path = state.path
        if (path.isNotEmpty()) {
            if (path.size == 1) {
                cameraState.animate(
                    CameraUpdateFactory.newLatLngZoom(path.last(), 17f),
                    600
                )
            } else {
                val b = LatLngBounds.builder()
                path.forEach { b.include(it) }
                cameraState.animate(
                    CameraUpdateFactory.newLatLngBounds(b.build(), 100),
                    600
                )
            }
        }
    }

    // Diagnóstico: se ficar muito tempo sem fix, avisa
    LaunchedEffect(state.isRecording, state.isLoading) {
        if (state.isRecording && state.isLoading) {
            kotlinx.coroutines.delay(8000)
            if (state.isRecording && state.isLoading) {
                vm.setError("Ainda sem sinal… verifique se a Localização está ativa e tente ir ao ar livre.")
            }
        }
    }

    // Para de gravar ao sair da tela (evita vazamento)
    DisposableEffect(Unit) {
        onDispose { vm.stopRecording() }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (!hasLocationPermission) {
                        requestPermissions.launch(permissions)
                        return@ExtendedFloatingActionButton
                    }
                    if (state.isRecording) {
                        vm.stopRecording()
                        vm.saveRoute()
                    } else {
                        ensureLocationThenStart(
                            isPrecise = isPrecise,
                            onOk = { vm.startRecording() },
                            onError = { vm.setError(it) }
                        )
                    }
                }
            ) { Text(if (state.isRecording) "Parar" else "Gravar") }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = false
                )
            ) {
                if (state.path.size >= 2) {
                    Polyline(points = state.path)
                }
                if (state.path.isNotEmpty()) {
                    Marker(
                        state = rememberMarkerState(position = state.path.first()),
                        title = "Início"
                    )
                    Marker(
                        state = rememberMarkerState(position = state.path.last()),
                        title = "Atual"
                    )
                }
            }

            // Painel de status
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(12.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    val title = when {
                        state.isRecording && state.isLoading -> "Iniciando GPS…"
                        state.isRecording -> "Gravando…"
                        else -> "Pronto"
                    }
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("Distância: ${"%.2f".format(state.distanceMeters / 1000)} km")
                    Text("Tempo: ${formatElapsed(state.elapsedMs)}")
                    state.errorMessage?.let { msg ->
                        Spacer(Modifier.height(6.dp))
                        Text(msg, color = MaterialTheme.colorScheme.error)
                    }
                    if (hasLocationPermission && !isPrecise) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Usando loc aproximada — ative 'Precisão exata' nas perm do app p melhor resultado.",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    if (!hasLocationPermission) {
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { requestPermissions.launch(permissions) }) {
                            Text("Permitir localização")
                        }
                    }
                }
            }

            // Botão limpar (quando parado)
            if (state.path.isNotEmpty() && !state.isRecording) {
                OutlinedButton(
                    onClick = { vm.clearRoute() },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) { Text("Limpar") }
            }
        }
    }
}

private fun formatElapsed(ms: Long): String {
    val total = ms / 1000
    val h = total / 3600
    val m = (total % 3600) / 60
    val s = total % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}
