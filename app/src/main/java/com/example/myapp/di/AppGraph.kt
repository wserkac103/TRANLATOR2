package com.example.myapp.di

import com.example.myapp.camera.CameraController
import com.example.myapp.ocr.OcrEngine
import com.example.myapp.translation.TranslationEngine

object AppGraph {
    val cameraController: CameraController by lazy { CameraController() }
    val ocrEngine: OcrEngine by lazy { OcrEngine() }
    val translationEngine: TranslationEngine by lazy { TranslationEngine() }
}
