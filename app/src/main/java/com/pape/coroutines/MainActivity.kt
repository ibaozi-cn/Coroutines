package com.pape.coroutines

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.delay
import one.hundred.experimental.*
import one.hundred.experimental.ui.JobLifecycleActivity
import one.hundred.experimental.ui.onClickStart

class MainActivity : JobLifecycleActivity() {

    private var count = 0

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

        clearText()

        textView.append("1：并发协程执行开始\n")

        taskAsync(1000) {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("3：并发协程1=====在线程“$threadName”中执行\n")
            }
        }

        taskAsync(3000) {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("4：并发协程2=====在线程“$threadName”中执行\n")
            }
        }

        textView.append("2：并发协程执行未阻塞执行\n")

    }

    private fun clearText() {
        textView.text = ""
    }

    /**
     * 协程按代码顺序执行
     */
    fun orderClick(view: View) {

        clearText()

        textView.append("1：顺序协程执行开始\n")

        taskBlockOnWorkThread {
            taskOrder {
                val threadName = Thread.currentThread().name
                textView.append("2：顺序协程1=====在线程“$threadName”中执行\n")
            }

            taskOrder {
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

        clearText()

        textView.append("1：并发协程执行后等待结果开始\n")

        taskRunOnUiThread {
            val job1 = taskAsync {
                val threadName = Thread.currentThread().name
                taskRunOnUiThread {
                    textView.append("3：并发协程1=====在线程“$threadName”中执行\n")
                }
                1
            }
            textView.append("4：等待执行结果“${job1.await()}”\n")
        }

        textView.append("2：并发协程执行后未阻塞\n")

    }

    /**
     * 执行心跳
     */
    fun buttonHeart(view: View) {

        clearText()

        textView.append("心跳开始\n")

        taskLaunch {
            //1秒重复1次，一共10次
            var count = 0
            taskHeartbeat(10, 1000) {
                taskRunOnUiThread {
                    textView.append("心跳${count++}\n")
                }
            }
        }
    }
}
