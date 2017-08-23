package one.hundred.experimental

import kotlinx.coroutines.experimental.*


/**
 * Created by zzy on 2017/8/21.
 *
 * 协程上下文：
 * 1. coroutineContext--------工作在主线程中 特点: 会让子协程跟随父协程cancel而取消
 * 2. CommonPool--------工作在子线程中 特点: 不会根随父协程cancel而取消
 * 3. Unconfined--------跟随当前线程 特点： 如果在主线程中执行调用delay以后便会切换到子线程kotlinx.coroutines.DefaultExecutor中执行
 * 4. newSingleThreadContext("MyOwnThread")---------自定义线程"MyOwnThread"
 *
 * 协程特点：
 * 1. 同一线程可以有多个协程
 * 2. 同一协程可以运行在不同上下文中 通过runBlocking 包子协程 run 实现 ： runBlocking(ctx1){ run(ctx2){ }  }
 */
object TaskCoroutines {

    /**
     * 在主线程中顺序执行，会阻塞代码往下执行
     */
    fun taskBlockOnMainThread(delayTime: Long = 0, job: suspend () -> Unit) = runBlocking {
        delay(delayTime)
        job()
    }

    /**
     * 在工作线程中顺序执行，会阻塞代码往下执行
     */
    fun taskBlockOnWorkThread(delayTime: Long = 0, job: suspend () -> Unit) = runBlocking(CommonPool) {
        delay(delayTime)
        job()
    }

    /**
     * 并发执行,不会阻塞代码继续执行
     * 特点带返回值
     */
    fun <T> taskAsync(delayTime: Long = 0, job: suspend () -> T) = async(CommonPool) {
        delay(delayTime)
        job()
    }

    /**
     * 并发执行,不会阻塞代码继续执行
     * 特点不带返回值
     */
    fun <T> taskLaunch(delayTime: Long = 0, job: suspend () -> T) = launch(CommonPool) {
        delay(delayTime)
        job()
    }

    /**
     * 顺序执行函数
     */
    suspend fun <T> taskOrder(delayTime: Long = 0, job: suspend () -> T) {
        delay(delayTime)
        job()
    }

    /**
     * 心跳执行 默认重复次数1次
     */
    suspend fun <T> taskHeartbeat(times: Int = 1, delayTime: Long = 0, job: suspend () -> T) = repeat(times) {
        delay(delayTime)
        job()
    }

}
