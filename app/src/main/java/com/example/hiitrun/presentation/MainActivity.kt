/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.hiitrun.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.hiitrun.R
import com.example.hiitrun.presentation.theme.HiitrunTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationState by mutableStateOf<Location?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp(
                location = locationState,
                onGetLocationClick = { checkAndRequestLocationPermissions() }
            )
        }
    }

    @SuppressLint("MissingPermission") // Permissions are checked before calling
    private fun fetchLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                locationState = location
            }
            .addOnFailureListener {
                // Handle failure, e.g., log error or show a message
                locationState = null // Or some error state
            }
    }

    private fun checkAndRequestLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                fetchLastLocation()
            }
            else -> {
                // The Composable will handle launching the permission request
                // This function is now primarily for the initial fetch if permissions are already there.
                // Or, it can be called after permissions are granted by the launcher's callback.
                // For simplicity in this structure, we'll rely on the Composable's launcher.
            }
        }
    }
}

@Composable
fun WearApp(location: Location?, onGetLocationClick: () -> Unit) {
    val context = LocalContext.current
    var permissionRequested by remember { mutableStateOf(false) }

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionRequested = true // Mark that a request has been made
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        ) {
            // Permission Granted. MainAcitvity's checkAndRequestLocationPermissions will call fetch.
            // To ensure fetch is called *after* grant by this launcher, we can call it directly here.
            // However, to keep FusedLocationProviderClient access within MainActivity:
            // We can trigger a re-evaluation or have a callback.
            // For simplicity, we rely on onGetLocationClick to re-trigger the check in MainActivity.
            // (context as? MainActivity)?.sendBroadcast(android.content.Intent("LOCATION_PERMISSION_GRANTED"))
            // Call onGetLocationClick directly if permissions were granted by this request.
            onGetLocationClick()

        } else {
            // Permission Denied.
            // locationState in MainActivity will remain null or unchanged.
        }
    }

    // Effect to check permissions when Composable enters or onGetLocationClick is triggered
    // if we want an initial check without a button press.
    // For this example, we'll use a button press to initiate.

    HiitrunTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LocationDisplay(location = location)
                Button(onClick = {
                    // Check permissions status
                    val fineLocationGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    val coarseLocationGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (fineLocationGranted || coarseLocationGranted) {
                        onGetLocationClick() // This will call checkAndRequestLocationPermissions in MainActivity
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }) {
                    Text(text = stringResource(R.string.get_location_button))
                }
            }
        }
    }
}

@Composable
fun LocationDisplay(location: Location?) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = if (location != null) {
            "Lat: ${location.latitude}\nLon: ${location.longitude}"
        } else {
            stringResource(R.string.location_not_available)
        }
    )
}

// Need to add these string resources in strings.xml
// R.string.get_location_button = "Get Location"
// R.string.location_not_available = "Location not available. Grant permission and try again."

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(location = null, onGetLocationClick = {})
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun LocationAvailablePreview() {
    val mockLocation = Location("mock").apply {
        latitude = 37.422
        longitude = -122.084
    }
    WearApp(location = mockLocation, onGetLocationClick = {})
}