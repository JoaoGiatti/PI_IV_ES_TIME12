package br.com.chase.ui.screens.route

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import br.com.chase.utils.formatDistance
import br.com.chase.utils.formatElapsed
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
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

    // -----------------------------------------------
    // Permissões de localização
    // -----------------------------------------------
    val hasFine = ContextCompat.checkSelfPermission(
        ctx, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    var hasLocationPermission by remember { mutableStateOf(hasFine) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (!granted) vm.clearError()
    }

    // -----------------------------------------------
    // Ajuste de GPS (LocationSettings)
    // -----------------------------------------------
    val settingsClient = remember { LocationServices.getSettingsClient(ctx) }
    val resolutionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            vm.startRecording()
        } else {
            vm.clearError()
        }
    }

    fun checkGpsAndStart() {
        val request = LocationRequest.Builder(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(request)
            .build()

        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener { vm.startRecording() }
            .addOnFailureListener { ex ->
                if (ex is ResolvableApiException) {
                    resolutionLauncher.launch(
                        IntentSenderRequest.Builder(ex.resolution).build()
                    )
                } else {
                    vm.setError("Ative o GPS nas configurações do sistema.")
                }
            }
    }

    val cameraPos = rememberCameraPositionState()

    LaunchedEffect(state.points) {
        if (state.points.isNotEmpty()) {
            cameraPos.animate(
                CameraUpdateFactory.newLatLngZoom(state.points.last(), 17f),
                600
            )
        }
    }

    state.errorMessage?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
            vm.clearError()
        }
    }

    state.successMessage?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
            vm.clearError()
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (!hasLocationPermission) {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        return@ExtendedFloatingActionButton
                    }

                    if (!state.isRecording) {
                        checkGpsAndStart()
                    } else {
                        vm.stopRecording()
                        vm.saveRoute()
                    }
                }
            ) {
                Text(
                    if (!state.isRecording) "Gravar rota"
                    else "Parar e salvar"
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPos,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true
                )
            ) {
                if (state.points.size >= 2) {
                    Polyline(points = state.points)
                }
                state.startLocation?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Início"
                    )
                }
                state.endLocation?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Atual"
                    )
                }
            }

            // --- Painel superior (distância / tempo) ---
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        if (state.isRecording) "Gravando..." else "Pronto",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Distância: ${formatDistance(state.distance)}")
                    Text("Tempo: ${formatElapsed(state.time)}")
                }
            }

            // --- Loading ---
            if (state.isLoading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = .4f))
                ) {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
