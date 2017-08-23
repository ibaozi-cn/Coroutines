package com.pape.coroutines

import UI
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.async
import one.hundred.experimental.TaskCoroutines
import taskRunOnUiThread
import kotlin.coroutines.experimental.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.text = ""
        Log.d("tag", "onCreate")
    }

    fun concurrentClick(view: View) {

        textView.append("并发协程执行开始\n")

        TaskCoroutines.taskConcurrent(1000) {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("并发协程1=====在线程“$threadName”中执行\n")
            }
        }

        TaskCoroutines.taskConcurrent(3000) {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("并发协程2=====在线程“$threadName”中执行\n")
            }
        }

        textView.append("并发协程执行不会阻塞当前代码执行\n")

    }

    fun orderClick(view: View) {

        textView.append("顺序协程执行开始\n")

        TaskCoroutines.taskOrderOnMainThread(3000) {
            val threadName = Thread.currentThread().name
            textView.append("顺序协程1=====在主线程“$threadName”中延迟3秒执行\n")
        }

        TaskCoroutines.taskOrderOnWorkThread(6000) {
            val threadName = Thread.currentThread().name
            taskRunOnUiThread {
                textView.append("顺序协程2=====在线程“$threadName”中延迟6秒执行\n")
            }
        }

        textView.append("顺序协程执行会阻塞当前代码执行\n")

    }

    fun buttonWait(view: View) {

        textView.append("并发协程执行后等待结果开始\n")

        val job1 = TaskCoroutines.taskConcurrent {
            val threadName = Thread.currentThread().name
            1
            taskRunOnUiThread {
                textView.append("并发协程1=====在线程“$threadName”中执行\n")
            }
        }

        TaskCoroutines.taskOrderOnMainThread(2000) {
            textView.append("等待执行结果“${job1.await()}”\n")
        }

        textView.append("并发协程执行后等待结果结束\n")

    }

    fun buttonHeart(view: View) {


    }
}
