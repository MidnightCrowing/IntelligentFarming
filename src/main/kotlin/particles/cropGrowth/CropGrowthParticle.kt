package com.midnightcrowing.particles.cropGrowth

import com.midnightcrowing.model.Point
import com.midnightcrowing.particles.composter.ComposterParticle

class CropGrowthParticle(
    position: Point,             // 当前位置
    velocity: Point,             // 速度向量
    size: Int,                   // 粒子大小
    lifetime: Double,            // 剩余生命
) : ComposterParticle(position, velocity, size, lifetime)