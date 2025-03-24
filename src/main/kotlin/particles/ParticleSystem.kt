package com.midnightcrowing.particles

import com.midnightcrowing.model.Point
import kotlin.random.Random

class ParticleSystem {
    private val particles = mutableListOf<Particle>()

    fun generateParticles(position: Point, color: FloatArray, count: Int) {
        for (i in 0 until count) {
            val randomPosition = Point(
                position.x + Random.nextDouble(-30.0, 30.0) + Random.nextDouble(-10.0, 10.0),
                position.y + Random.nextDouble(-15.0, 40.0) + Random.nextDouble(-5.0, 5.0)
            )
            val velocity = Point(
                Random.nextDouble(-80.0, 80.0),
                Random.nextDouble(75.0, 150.0)
            )
            val life = Random.nextFloat() * 0.5
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