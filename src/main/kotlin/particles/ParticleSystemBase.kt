package com.midnightcrowing.particles

import com.midnightcrowing.model.Point
import kotlin.random.Random

/**
 * 粒子系统管理类
 * 负责生成、更新和渲染粒子效果
 */
abstract class ParticleSystemBase<T : ParticleBase> {
    abstract val particles: MutableList<T>

    // region 生成随机位置的公共方法
    /**
     * 生成方块的随机位置。
     *
     * 该方法根据提供的原点 (`origin`) 和位置变化范围 (`positionVariation`)，
     * 随机生成一个新的位置。生成的随机位置会在原点附近的一个区域内，
     * 该区域的大小由 `positionVariation` 控制，并且在 X 和 Y 坐标上会有额外的偏移。
     *
     * @param origin 方块的原点位置，用于确定随机位置的基准点。
     * @param positionVariation 控制方块生成位置的随机范围。X 和 Y 坐标的变化范围会根据该值来调整。
     * @return 返回一个新的 `Point` 对象，表示生成的随机位置。
     */
    protected fun generateRandomPositionForBlock(origin: Point, positionVariation: Point): Point {
        return Point(
            x = origin.x + Random.nextDouble(
                from = -positionVariation.x / 2,
                until = positionVariation.x / 2
            ) + Random.nextDouble(-10.0, 10.0),
            y = origin.y + Random.nextDouble(
                from = -positionVariation.y / 11 * 3,
                until = positionVariation.y / 11 * 8
            ) + Random.nextDouble(-5.0, 5.0)
        )
    }

    /**
     * 生成基于两个向量的随机位置。
     *
     * 该方法根据提供的原点 (`origin`) 和两个方向向量 (`areaX` 和 `areaY`)，
     * 随机生成一个新的位置。生成的随机位置会在以原点为基准点的平行四边形区域内，
     * 该区域的大小和方向由 `areaX` 和 `areaY` 控制。
     *
     * @param origin 原点位置，用于确定随机位置的基准点。
     * @param areaX 平行四边形区域的 X 方向向量，决定生成位置在 X 轴上的范围。
     * @param areaY 平行四边形区域的 Y 方向向量，决定生成位置在 Y 轴上的范围。
     * @return 返回一个新的 `Point` 对象，表示生成的随机位置，该位置位于由 `areaX` 和 `areaY` 定义的区域内。
     */
    protected fun generateRandomPositionInArea(origin: Point, areaX: Point, areaY: Point): Point {
        return origin + areaX * Random.nextDouble() + areaY * Random.nextDouble()
    }

    // endregion

    /**
     * 更新所有粒子的状态
     * @param deltaTime 时间步长
     */
    fun update(deltaTime: Float) {
        particles.forEach { it.update(deltaTime) }
        particles.removeIf { it.isDead() } // 移除生命周期结束的粒子
    }

    /**
     * 渲染所有粒子
     */
    open fun render() = particles.forEach { it.render() }
}