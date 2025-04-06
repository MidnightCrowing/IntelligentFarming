package com.midnightcrowing.utils

/**
 * 定时器工具类，用于在基于 tick 的游戏循环中判断某个操作是否应该执行。
 *
 * @param interval 间隔时间（毫秒），表示多久执行一次。
 */
class Timer(private val interval: Long) {
    // 上次触发时的 tick 时间
    private var lastTriggerTime: Long = 0

    /**
     * 判断当前是否应该执行任务，如果满足间隔时间则返回 true，并更新 lastTriggerTime。
     *
     * @param currentTick 当前游戏运行总时间（毫秒），通常来自 GameTick.tick。
     * @return 是否应该执行任务。
     */
    fun shouldRun(currentTick: Long = GameTick.tick): Boolean {
        return if (currentTick - lastTriggerTime >= interval) {
            lastTriggerTime = currentTick
            true
        } else {
            false
        }
    }

    /**
     * 重置定时器，使其下次立即触发。
     */
    fun reset() {
        lastTriggerTime = 0
    }

    /**
     * 强制更新时间，使其下次必须等待完整间隔才会触发。
     *
     * @param currentTick 当前游戏运行时间。
     */
    fun skip(currentTick: Long = GameTick.tick) {
        lastTriggerTime = currentTick
    }
}

