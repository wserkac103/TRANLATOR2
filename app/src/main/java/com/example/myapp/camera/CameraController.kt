package com.example.myapp.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalyzer
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias ImageAnalysisCallback = (imageAnalyzer: ImageAnalyzer) -> Unit

class CameraController(context: Context) {
    private val applicationContext = context.applicationContext
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var previewUseCase: Preview? = null
    private var imageAnalysisUseCase: ImageAnalysis? = null
    private var cameraExecutor: ExecutorService? = null

    private var imageAnalysisCallback: ImageAnalysisCallback? = null

    init {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun setImageAnalysisCallback(callback: ImageAnalysisCallback) {
        imageAnalysisCallback = callback
    }

    fun start(lifecycleOwner: LifecycleOwner, previewView: androidx.camera.view.PreviewView) {
        if (cameraProvider != null) {
            Timber.d("Camera is already started")
            return
        }

        cameraProviderFuture = ProcessCameraProvider.getInstance(applicationContext)
        cameraProviderFuture?.addListener({
            try {
                cameraProvider = cameraProviderFuture?.get()

                // Create and setup Preview use case
                previewUseCase = Preview.Builder().build()
                previewUseCase?.setSurfaceProvider(previewView.surfaceProvider)

                // Create and setup ImageAnalysis use case
                imageAnalysisUseCase = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply {
                        setAnalyzer(cameraExecutor!!) { image ->
                            imageAnalysisCallback?.invoke { _ ->
                                image.close()
                            } ?: image.close()
                        }
                    }

                // Select back camera
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                // Unbind all use cases before rebinding
                cameraProvider?.unbindAll()

                // Bind use cases to camera
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    imageAnalysisUseCase
                )

                Timber.d("Camera started successfully")
            } catch (exc: Exception) {
                Timber.e(exc, "Failed to bind camera")
            }
        }, { it?.execute() ?: run { Timber.e("Camera executor unavailable") } })
    }

    fun stop() {
        try {
            cameraProvider?.unbindAll()
            cameraProvider = null
            cameraProviderFuture = null
            previewUseCase = null
            imageAnalysisUseCase = null
            Timber.d("Camera stopped")
        } catch (exc: Exception) {
            Timber.e(exc, "Failed to stop camera")
        }
    }

    fun release() {
        stop()
        cameraExecutor?.shutdown()
        cameraExecutor = null
    }
}
