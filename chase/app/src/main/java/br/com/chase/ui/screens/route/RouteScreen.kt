package br.com.chase.ui.screens.route

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.ui.components.RouteNameDialog
import br.com.chase.ui.theme.PrimaryRainbow
import br.com.chase.utils.calcularPace
import br.com.chase.utils.createBalloonBitmap
import br.com.chase.utils.formatAverageSpeed
import br.com.chase.utils.formatCalories
import br.com.chase.utils.formatDistance
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    paddingValues: PaddingValues,
    vm: RouteViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current

    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-22.834560, -47.052783),
            15f
        )
    }
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val fusedClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var showNameDialog by remember { mutableStateOf(false) }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return

                vm.onLocationReceived(
                    LatLng(loc.latitude, loc.longitude),
                    loc.accuracy,
                    loc.time,
                    loc.speed
                )
            }
        }
    }
    val userMarkerBitmap by produceState<Bitmap?>(initialValue = null, state.user?.photoUrl) {
        if (state.user?.photoUrl != null) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(state.user?.photoUrl)
                .allowHardware(false)
                .build()

            val result = loader.execute(request)
            value = (result.drawable as? BitmapDrawable)?.bitmap
        }
    }
    val balloonBitmap = userMarkerBitmap?.let { createBalloonBitmap(it) }
    val markerIcon = balloonBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }

    LaunchedEffect(Unit) {
        locationPermission.launchPermissionRequest()
    }
    LaunchedEffect(state.isRecording, locationPermission.status.isGranted) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (state.isRecording && hasPermission) {
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L // intervalo "desejado" em ms
            )
                .setMinUpdateIntervalMillis(2000L)      // intervalo mínimo
                .setMinUpdateDistanceMeters(5f)       // deslocamento mínimo em metros
                .build()

            fusedClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            // Não está gravando ou sem permissão: garante que parou
            fusedClient.removeLocationUpdates(locationCallback)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            fusedClient.removeLocationUpdates(locationCallback)
        }
    }
    LaunchedEffect(state.route.points) {
        if (state.route.points.isNotEmpty()) {
            val last = state.route.points.last()
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(last, 20f, 55f, 0f)
                ),
                durationMs = 300
            )
        }
    }
    LaunchedEffect(state.competitionPoints) {
        if (state.competitionPoints.isNotEmpty() && state.route.points.isEmpty()) {
            val first = state.competitionPoints.first()
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(first, 20f, 55f, 0f)
                ),
                durationMs = 300
            )
        }
    }
    LaunchedEffect(state.successMessage, state.errorMessage) {
        state.successMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            vm.clearMessages()
        }
        state.errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            vm.clearMessages()
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContainerColor = MaterialTheme.colorScheme.onPrimary,
        sheetContent = {

            val isCompetePreview = state.mode == RunMode.COMPETE && !state.isRecording

            if (isCompetePreview) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Distância", fontWeight = FontWeight.Bold)
                            Text(formatDistance(state.competitionRoute!!.distance))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Constância", fontWeight = FontWeight.Bold)
                            Text(calcularPace(state.competitionRoute!!.distance, state.competitionRoute!!.recordTime)) // ajuste para a sua prop
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Tempo recorde", fontWeight = FontWeight.Bold)
                            Text(state.competitionRoute!!.recordTime)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Velocidade Média", fontWeight = FontWeight.Bold)
                            Text(formatAverageSpeed(state.competitionRoute!!.bestAverageSpeed))
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Gasto", fontWeight = FontWeight.Bold)
                            Text(formatCalories(state.competitionRoute!!.estimatedCalories))
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            vm.startRun()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(PrimaryRainbow),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text("Competir!")
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column {
                            Text("Tempo", fontWeight = FontWeight.Bold)
                            Text(state.route.recordTime)
                        }
                        Column {
                            Text("Distância", fontWeight = FontWeight.Bold)
                            Text(formatDistance(state.route.distance))
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (!state.isRecording) {
                                vm.startRun()
                            } else {
                                vm.stopRun()

                                when (state.mode) {
                                    RunMode.RECORD -> showNameDialog = true
                                    RunMode.COMPETE -> vm.saveCompetitionRun()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(PrimaryRainbow),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        val label = when (state.mode) {
                            RunMode.RECORD ->
                                if (!state.isRecording) "Iniciar" else "Parar"

                            RunMode.COMPETE ->
                                if (!state.isRecording) "Iniciar prova" else "Parar prova"
                        }

                        Text(label)
                    }
                }
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                if (state.competitionPoints.size > 1) {
                    Polyline(
                        points = state.competitionPoints,
                        width = 12f,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (state.route.points.size > 1) {
                    Polyline(
                        points = state.route.points,
                        width = 12f
                    )
                }
                if (state.route.points.isNotEmpty() && markerIcon != null) {
                    Marker(
                        state = MarkerState(position = state.route.points.last()),
                        icon = markerIcon,
                        anchor = Offset(0.5f, 1f),
                        flat = true
                    )
                }
            }
            if (state.countdown != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x88000000)),
                    contentAlignment = Alignment.Center
                ) {
                    val text = if (state.countdown == 0) "GO!" else state.countdown.toString()

                    Text(
                        text = text,
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Abrir o popup
            if (showNameDialog) {
                RouteNameDialog(
                    onConfirm = { name ->
                        vm.updateRouteName(name)

                        showNameDialog = false
                        vm.saveRun()
                    },
                    onDismiss = { showNameDialog = false }
                )
            }
        }
    }
}
