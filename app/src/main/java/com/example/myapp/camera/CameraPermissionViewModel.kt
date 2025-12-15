package com.example.myapp.camera

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class CameraPermissionState(
    val cameraPermissionGranted: Boolean = false,
    val audioPermissionGranted: Boolean = false
) {
    val allPermissionsGranted: Boolean
        get() = cameraPermissionGranted && audioPermissionGranted
}

class CameraPermissionViewModel : ViewModel() {
    private val _permissionState = mutableStateOf(CameraPermissionState())
    val permissionState: State<CameraPermissionState> = _permissionState

    fun setCameraPermissionGranted(granted: Boolean) {
        _permissionState.value = _permissionState.value.copy(cameraPermissionGranted = granted)
    }

    fun setAudioPermissionGranted(granted: Boolean) {
        _permissionState.value = _permissionState.value.copy(audioPermissionGranted = granted)
    }

    fun setAllPermissionsGranted(cameraGranted: Boolean, audioGranted: Boolean) {
        _permissionState.value = CameraPermissionState(
            cameraPermissionGranted = cameraGranted,
            audioPermissionGranted = audioGranted
        )
    }
}
