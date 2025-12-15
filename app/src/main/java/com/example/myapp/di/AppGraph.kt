package com.example.myapp.di

import android.content.Context
import com.example.myapp.camera.CameraController
import com.example.myapp.camera.CameraPermissionViewModel
import com.example.myapp.ocr.OcrEngine
import com.example.myapp.translation.TranslationEngine

object AppGraph {
    private var applicationContext: Context? = null

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    val cameraController: CameraController by lazy {
        requireNotNull(applicationContext) { "AppGraph must be initialized with a context" }
        CameraController(applicationContext!!)
    }

    val cameraPermissionViewModel: CameraPermissionViewModel by lazy { CameraPermissionViewModel() }

    val ocrEngine: OcrEngine by lazy { OcrEngine() }

    val translationEngine: TranslationEngine by lazy { TranslationEngine() }
}
