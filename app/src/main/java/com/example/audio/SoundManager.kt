package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.ToneGenerator
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (_: Exception) {}
    }

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun vibrate(durationMs: Long = 60) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    fun playWhistle() {
        vibrate(100)
        scope.launch {
            try {
                // Synthesize rapid referee whistle sound (2500Hz - 2900Hz alternating tone with flutter)
                val sampleRate = 22050
                val durationSeconds = 0.45
                val numSamples = (sampleRate * durationSeconds).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    // Trill / flutter modulation
                    val freq = 2650.0 + 200.0 * sin(2.0 * Math.PI * 30.0 * time)
                    val envelope = if (i < 500) i / 500.0 else if (i > numSamples - 1500) (numSamples - i) / 1500.0 else 1.0
                    val sample = (sin(2.0 * Math.PI * freq * time) * envelope * Short.MAX_VALUE * 0.7).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer, sampleRate)
            } catch (_: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_PBX_L, 300)
            }
        }
    }

    fun playBoomTaakhGavel() {
        vibrate(200)
        scope.launch {
            try {
                // Synthesize dramatic deep "BOOM TAAKH!" impact: Low punch + pitch decay + metallic snap
                val sampleRate = 22050
                val durationSeconds = 0.65
                val numSamples = (sampleRate * durationSeconds).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    // Pitch drops rapidly from 220Hz down to 45Hz
                    val freq = 45.0 + 175.0 * kotlin.math.exp(-time * 9.0)
                    val decay = kotlin.math.exp(-time * 5.0)
                    // High snap click at start
                    val click = if (i < 400) sin(2.0 * Math.PI * 900.0 * time) * 0.5 else 0.0
                    val sample = ((sin(2.0 * Math.PI * freq * time) + click) * decay * Short.MAX_VALUE * 0.85).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer, sampleRate)
            } catch (_: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 400)
            }
        }
    }

    fun playCoinBid() {
        vibrate(30)
        scope.launch {
            try {
                val sampleRate = 22050
                val durationSeconds = 0.2
                val numSamples = (sampleRate * durationSeconds).toInt()
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val freq = if (time < 0.1) 987.77 else 1318.51 // B5 to E6 chime
                    val decay = kotlin.math.exp(-time * 8.0)
                    val sample = (sin(2.0 * Math.PI * freq * time) * decay * Short.MAX_VALUE * 0.6).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                playBuffer(buffer, sampleRate)
            } catch (_: Exception) {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 120)
            }
        }
    }

    fun playPenaltyAwarded() {
        vibrate(150)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 350)
            } catch (_: Exception) {}
        }
    }

    private fun playBuffer(buffer: ShortArray, sampleRate: Int) {
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2.coerceAtLeast(minBufferSize))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(buffer, 0, buffer.size)
        track.play()
    }
}
