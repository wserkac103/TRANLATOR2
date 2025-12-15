package com.example.myapp.tts

import android.content.Context
import android.speech.tts.TextToSpeech

class TtsEngine(
    context: Context,
) : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        // TODO: Configure TTS language/voice once user settings exist.
    }

    fun shutdown() {
        tts.shutdown()
    }
}
