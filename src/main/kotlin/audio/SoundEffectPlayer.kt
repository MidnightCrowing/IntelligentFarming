package com.midnightcrowing.audio

import com.midnightcrowing.resource.AudioResource
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent
import kotlin.concurrent.thread

object SoundEffectPlayer {

    fun play(id: String) {
        thread {
            try {
                val audioInputStream = getAudioInputStreamById(id)
                val clip = AudioSystem.getClip().apply {
                    open(audioInputStream)
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
}
