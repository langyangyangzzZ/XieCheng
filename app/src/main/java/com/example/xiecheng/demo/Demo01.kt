package com.example.xiecheng

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *    author : Tiaw.
 *    date   : 4/21/21
 *    博客：https://blog.csdn.net/weixin_44819566
 *    desc   : 非阻塞式Demo1
 */
fun main() {
    //非阻塞式   类似于守护线程(main线程结束，他跟着结束)
    GlobalScope.launch {
        //等待1秒
        delay(1000)
        println("launch")
    }

    //主线程
    println("A")
    //睡眠0.2秒
    Thread.sleep(2000)
    //主线程
    println("B")

    /**
       结果:
            A
            launch
            B
     */
}