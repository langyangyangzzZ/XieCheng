package com.example.xiecheng.demo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *    author : Tiaw.
 *    date   : 4/21/21
 *    博客：https://blog.csdn.net/weixin_44819566
 *    desc   :
 */
suspend fun main() {
    //非阻塞式协程
    val job = GlobalScope.launch {

        //轮训100次 每10ms 打印一次
        repeat(1000) {
            delay(10)
            println("$it")
        }
    }

    println("A")

    //只等待100ms
    Thread.sleep(100)

    //协程取消 会有一点点时间差，不是非常准确
//    job.cancel()

    //协程取消 立马停止，非常准确
    job.cancelAndJoin()
}