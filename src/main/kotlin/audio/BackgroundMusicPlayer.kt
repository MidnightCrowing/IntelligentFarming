package com.midnightcrowing.audio

import com.midnightcrowing.audio.AudioResource.getAudioStream
import com.midnightcrowing.resource.ResourceLocation
import javax.sound.sampled.*
import kotlin.concurrent.thread

object BackgroundMusicPlayer {
    private var clip: Clip? = null
    private var currentTrackName: String? = null
    private var isPaused = false
    private var pausePosition = 0
    private var loop = false

    private var volumeControl: FloatControl? = null
    private var fadeThread: Thread? = null
    private var queuedTracks: MutableList<ResourceLocation> = mutableListOf()

    fun play(audio: ResourceLocation, loop: Boolean = true, fadeIn: Boolean = true) {
        val name: String = audio.toString()
        val audioInputStream: AudioInputStream = getAudioStream(audio) ?: return

        if (currentTrackName == name && clip?.isActive == true) return

        stop(fadeOut = true)

        this.loop = loop
        this.currentTrackName = name

        thread {
            try {
                val newClip = AudioSystem.getClip().apply {
                    open(audioInputStream)

                    addLineListener { event ->
                        when (event.type) {
                            LineEvent.Type.STOP -> {
                                if (!isPaused && !this@BackgroundMusicPlayer.loop) {
                                    playNextInQueue()
                                }
                            }

                            LineEvent.Type.CLOSE -> {
                                // 处理资源清理
                            }

                            else -> {}
                        }
                    }
                }

                clip?.close()
                clip = newClip
                volumeControl = newClip.getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl

                if (fadeIn) {
                    fadeVolume(newClip, from = -80f, to = 0f, durationMs = 1000) {
                        if (loop) {
                            newClip.loop(Clip.LOOP_CONTINUOUSLY)
                        } else {
                            newClip.start()
                        }
                    }
                } else {
                    volumeControl?.value = 0f
                    if (loop) {
                        newClip.loop(Clip.LOOP_CONTINUOUSLY)
                    } else {
                        newClip.start()
                    }
                }
            } catch (e: Exception) {
                println("播放失败: ${e.message}")
            }
        }
    }

    fun stop(fadeOut: Boolean = false) {
        fadeThread?.interrupt()
        clip?.let { currentClip ->
            if (fadeOut) {
                fadeVolume(currentClip, from = volumeControl?.value ?: 0f, to = -80f, durationMs = 1000) {
                    currentClip.stop()
                    currentClip.close()
                }
            } else {
                currentClip.stop()
                currentClip.close()
            }
        }
        clip = null
        currentTrackName = null
        isPaused = false
        pausePosition = 0
    }

    fun pause() {
        clip?.let {
            if (it.isRunning) {
                pausePosition = it.framePosition
                isPaused = true
                it.stop()
            }
        }
    }

    fun resume() {
        clip?.let {
            if (isPaused) {
                it.framePosition = pausePosition
                isPaused = false
                it.start()
            }
        }
    }

    fun queue(location: ResourceLocation) {
        queuedTracks.add(location)
    }

    fun setVolume(linear: Double) {
        val db = (20 * kotlin.math.log10(linear.coerceIn(0.0001, 1.0))).toFloat()
        volumeControl?.let {
            it.value = db.coerceIn(it.minimum, it.maximum)
        }
    }

    private fun playNextInQueue() {
        queuedTracks.removeFirstOrNull()?.let { location ->
            play(location, loop = false)
        }
    }

    private fun fadeVolume(
        clip: Clip,
        from: Float,
        to: Float,
        durationMs: Long,
        onComplete: (() -> Unit)? = null,
    ) {
        fadeThread?.interrupt()
        fadeThread = thread {
            try {
                val steps = 50
                val delay = durationMs / steps
                val delta = (to - from) / steps

                volumeControl?.value = from
                repeat(steps) {
                    if (volumeControl?.value == null) return@thread
                    Thread.sleep(delay)
                    volumeControl?.value = from + delta * (it + 1)
                }
                onComplete?.invoke()
            } catch (_: InterruptedException) {
            }
        }
    }
}