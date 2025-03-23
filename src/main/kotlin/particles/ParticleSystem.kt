package com.midnightcrowing.particles

import com.midnightcrowing.model.Point
import kotlin.random.Random

class ParticleSystem {
    private val particles = mutableListOf<Particle>()

    fun generateParticles(position: Point, color: FloatArray, count: Int) {
        for (i in 0 until count) {
//            println("生成粒子 $i")
            val randomPosition = Point(
                position.x + Random.nextDouble(-30.0, 30.0),
                position.y + Random.nextDouble(-30.0, 30.0)
            )
            val velocity = Point(
                Random.nextDouble(-20.0, 20.0),
                Random.nextDouble(-20.0, 0.0)
            )
            val life = Random.nextFloat() * 1.0
            particles.add(Particle(randomPosition, velocity, color, life))
        }
    }

    fun update(deltaTime: Float) {
        particles.forEach { it.update(deltaTime) }
        particles.removeIf { it.life <= 0 }
    }

    fun render() {
        particles.forEach { it.render() }
    }
}