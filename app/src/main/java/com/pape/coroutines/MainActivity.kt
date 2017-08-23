package com.pape.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.delay
import one.hundred.experimental.TaskCoroutines
import one.hundred.experimental.ui.JobActivity
import one.hundred.experimental.ui.onClickStart
import taskRunOnUiThread

class MainActivity : JobActivity() {

    var count = 0

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        textView.onClick {
//            Log.d("onClick", "onClick${count++}")
//        }
        textView.onClickStart {
            delay(3000) //三秒内的点击只有第一次有效
            Log.d("onClickStart", "onClickStart${count++}")
        }
    }

    /**
     * 两个协程并发执行
     */
    fun concurrentClick(view: View) {

        textView.append("1：并发协程执行开始\n")

        TaskCoroutines.taskAsync(1000) {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("2：并发协程1=====在线程“$threadName”中执行\n")
            }
        }

        TaskCoroutines.taskAsync(3000) {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("3：并发协程2=====在线程“$threadName”中执行\n")
            }
        }

        textView.append("4：并发协程执行不会阻塞当前代码执行\n")

    }

    /**
     * 协程按代码顺序执行
     */
    fun orderClick(view: View) {

        textView.append("1：顺序协程执行开始\n")

        TaskCoroutines.taskBlockOnMainThread {
            TaskCoroutines.taskOrder {
                val threadName = Thread.currentThread().name
                textView.append("2：顺序协程1=====在线程“$threadName”中执行\n")
            }

            TaskCoroutines.taskOrder {
                val threadName = Thread.currentThread().name
                textView.append("3：顺序协程2=====在线程“$threadName”中执行\n")
            }
        }

        textView.append("4：顺序协程执行会阻塞当前代码执行\n")

    }

    /**
     * 两个异步协程 第二个需要等待第一个完成后执行
     */
    fun buttonWait(view: View) {

        textView.append("1：并发协程执行后等待结果开始\n")

        val job1 = TaskCoroutines.taskAsync {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("2：并发协程1=====在线程“$threadName”中执行\n")
            }
            1
        }

        TaskCoroutines.taskAsync {
            taskRunOnUiThread {
                textView.append("3：等待执行结果“${job1.await()}”\n")
            }
        }

        textView.append("4：并发协程执行后等待结果结束\n")

    }

    fun buttonHeart(view: View) {
        textView.append("心跳开始\n")
        TaskCoroutines.taskBlockOnMainThread {
            //1秒重复1次，一共100次
            var count = 0
            TaskCoroutines.taskHeartbeat(100, 1000) {
                textView.append("心跳${count++}\n")
            }
        }
    }
}
