package com.example.myapp.ui

import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.myapp.camera.CameraController
import com.example.myapp.camera.CameraPermissionViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    permissionViewModel: CameraPermissionViewModel? = null,
    cameraController: CameraController? = null,
    onLifecycleStop: (() -> Unit)? = null,
    onLifecycleDestroy: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState = permissionViewModel?.permissionState?.value
    val previewView = remember { PreviewView(context) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (permissionState?.allPermissionsGranted == true && cameraController != null) {
                        cameraController.start(lifecycleOwner, previewView)
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    onLifecycleStop?.invoke()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    onLifecycleDestroy?.invoke()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (permissionState?.allPermissionsGranted == true) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { previewView }
            )
        } else {
            Text(
                text = if (permissionState != null) {
                    "Camera and microphone permissions required"
                } else {
                    "Loading..."
                },
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
