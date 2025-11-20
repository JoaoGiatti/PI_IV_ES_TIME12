package br.com.chase.ui.screens.route

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
import br.com.chase.ui.theme.PrimaryRainbow
import br.com.chase.utils.createBalloonBitmap
import br.com.chase.utils.formatDistance
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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
    LaunchedEffect(state.isRecording) {
        if (state.isRecording) {
            if (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val client = LocationServices.getFusedLocationProviderClient(context)
                val request = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    1000
                ).build()

                client.requestLocationUpdates(
                    request,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            val loc = result.lastLocation ?: return
                            vm.addLocation(
                                LatLng(loc.latitude, loc.longitude),
                                loc.accuracy
                            )
                        }
                    },
                    Looper.getMainLooper()
                )
            }
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

    BottomSheetScaffold(
        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContainerColor = MaterialTheme.colorScheme.onPrimary,
        sheetContent = {
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
                            vm.saveRun()
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
                    Text(if (!state.isRecording) "Iniciar" else "Parar")
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
        }
    }
}
