package com.example.myapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapp.di.AppGraph
import com.example.myapp.ui.MainScreen
import com.example.myapp.ui.theme.MyAppTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppGraph.initialize(this)

        val permissionViewModel = AppGraph.cameraPermissionViewModel
        val cameraController = AppGraph.cameraController

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
            permissionViewModel.setAllPermissionsGranted(cameraGranted, audioGranted)
            Timber.d("Permissions - Camera: $cameraGranted, Audio: $audioGranted")
        }

        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        permissionLauncher.launch(requiredPermissions)

        setContent {
            MyAppTheme {
                MainScreen(
                    permissionViewModel = permissionViewModel,
                    cameraController = cameraController,
                    onLifecycleStop = { cameraController.stop() },
                    onLifecycleDestroy = { cameraController.release() }
                )
            }
        }
    }
}
