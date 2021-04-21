package com.example.xiecheng.demo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 *    author : Tiaw.
 *    date   : 4/21/21
 *    博客：https://blog.csdn.net/weixin_44819566
 *    desc   : 阻塞式协程Demo1
 */
fun main(): Unit = runBlocking {
    // 非阻塞式协程
    GlobalScope.launch {
        //等待1秒
        delay(1000)
        println("launch")
    }

    //阻塞执行
    println("A")

    //阻塞执行等待2秒
    delay(2000)

    //阻塞执行
    println("B")

    /**
     * 结果:
         A
        launch
        B
     */
}