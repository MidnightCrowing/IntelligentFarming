package com.midnightcrowing.audio

import com.midnightcrowing.config.AppConfig
import com.midnightcrowing.resource.AudioResource
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineEvent
import kotlin.concurrent.thread
import kotlin.math.log10

object SoundEffectPlayer {

    fun play(id: String) {
        thread {
            try {
                val audioInputStream = getAudioInputStreamById(id)
                val clip = AudioSystem.getClip().apply {
                    open(audioInputStream)
                    setVolumeDb(getVolumeDb(id))
                    start()
                    addLineListener { event ->
                        if (event.type == LineEvent.Type.STOP || event.type == LineEvent.Type.CLOSE) {
                            stop()
                            close()
                        }
                    }
                }
            } catch (e: Exception) {
                println("播放音效失败: ${e.message}")
            }
        }
    }

    private fun getAudioInputStreamById(id: String): AudioInputStream {
        return AudioResource.getById(id).let {
            if (it is AudioResource.Group) it.getRandomAudioStream()
            else it.getAudioStream()
        }
    }

    private fun getVolumeDb(id: String): Float {
        return when (id.substringBefore('.')) {
            "block", "entity", "item" -> {
                linearToDb(AppConfig.MAIN_VOLUME * AppConfig.SOUND_VOLUME)
            }

            "ui" -> {
                linearToDb(AppConfig.MAIN_VOLUME * AppConfig.UI_VOLUME)
            }

            else -> linearToDb(1.0)
        }
    }

    private fun linearToDb(linear: Double): Float {
        return (20 * log10(linear.coerceIn(0.0001, 1.0))).toFloat()
    }

    private fun javax.sound.sampled.Clip.setVolumeDb(db: Float) {
        (getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl)?.apply {
            value = db.coerceIn(minimum, maximum)
        }
    }
}