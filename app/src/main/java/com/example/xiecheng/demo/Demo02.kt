package com.example.xiecheng.demo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *    author : Tiaw.
 *    date   : 4/21/21
 *    博客：https://blog.csdn.net/weixin_44819566
 *    desc   : 非阻塞式 Demo2
 */
fun main() {
    //非阻塞式   类似于守护线程(main线程结束，他跟着结束)
    GlobalScope.launch {
        //等待1秒
        delay(1000)
        println("launch")
    }

    println("A")

    //睡眠0.2秒
    Thread.sleep(200)

    println("B")

    //GlobalScope.launch 守护线程,跟着main线程的结束而结束
    /**
    结果:
    A
    B
     */
}