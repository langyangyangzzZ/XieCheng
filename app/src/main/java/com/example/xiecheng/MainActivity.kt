package com.example.xiecheng

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.xiecheng.bean.DataBean
import com.example.xiecheng.databinding.MainActivityDataBinDing
import com.example.xiecheng.http.buildHttp
import com.example.xiecheng.http.toast
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observer
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var databing: MainActivityDataBinDing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databing = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)


    }

    val TAG = "MainActivity协程:"


    /**
     * 协程，线程 的区别：
     * 1。线程 进程 都是同步机制，而协程则是异步
     * 2。一个线程可以多个协程，一个进程也可以单独拥有多个协程。
     * 3。线程是抢占式，而协程是非抢占式的，所以需要用户自己释放使用权来切换到其他协程，
     *      因此同一时间其实只有一个协程拥有运行权，相当于单线程的能力。
     * 4。在进程眼中线程是轻量级的，在线程眼中，协程是更轻量级的
     *
     * 参考自：https://blog.csdn.net/fadbgfnbxb/article/details/88787361
     */

    /**
     * 测试1
     */                        //runBlocking用来切换到协程环境
    fun testBt1(view: View) = runBlocking {

        databing.textView.text = "launch外"
        // launch是内协程 用来开启携程

        //默认是主协程 执行
        launch {
            //当前在主协程
            Log.i(TAG, "主协程: ${Thread.currentThread().name}")
            databing.textView.text = "launch里面"
        }

        /**
         * Dispatchers.Main：使用这个调度器在 Android 主线程上运行一个协程。
         *                  可以用来更新UI 。在UI线程中执行
         *
         * Dispatchers.IO：这个调度器被优化在主线程之外执行磁盘或网络 I/O。在线程池中执行
         *
         * Dispatchers.Default：这个调度器经过优化，可以在主线程之外执行 cpu 密集型的工作。
         *                      例如对列表进行排序和解析 JSON。在线程池中执行。
         *
         * Dispatchers.Unconfined：在调用的线程直接执行。
         */
        launch(Dispatchers.Unconfined) {
            //这个消息在子线程中
            //DefaultDispatcher-worker-1
            Log.i(TAG, "IO线程1: ${Thread.currentThread().name}")

            //重复执行
            repeat(10) {
                delay(1000)
                Log.i(TAG, "IO线程2: 当前下标为: $it")
            }
        }
    }


    //请求网络数据
    fun testBt2(view: View) = runBlocking {
        launch {
            //请求网络数据
            buildHttp(this@MainActivity, databing.textView)
        }
    }


    /**
     * 模拟Rxjava无限切换线程操作
     *
     * runBlocking 是阻塞式的
     */
    fun testBt3(view: View) = runBlocking {

        /**
         * 阻塞式切换线程(同步执行)
         */
        launch {

            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("正在执行阻塞式切换线程...")
            dialog.show()

            withContext(Dispatchers.IO) {
                //子线程
                Thread.sleep(2000)
                Log.i("testBt1", "${Thread.currentThread().name}")
            }

            //主线程
            Log.i("testBt2", "${Thread.currentThread().name}")

            withContext(Dispatchers.IO) {
                //子线程
                Thread.sleep(2000)
                Log.i("testBt3", "${Thread.currentThread().name}")
            }
            Log.i("testBt4", "${Thread.currentThread().name}")
        }
    }

    /**
     * 阻塞式同步切换线程(ANR测试)
     */
    fun testBt5(view: View) = runBlocking {
        launch {
            withContext(Dispatchers.IO) {
                //子线程
               delay(1000)
            }

            withContext(Dispatchers.IO){
                //子线程
                delay(3000)
            }
            withContext(Dispatchers.IO) {
                //子线程
                delay(10000)
            }

            withContext(Dispatchers.IO){
                //子线程
                delay(4000)
            }
        }
    }

    /**
     * 非阻塞式切换线程(异步消息)
     */
    fun testBt4(view: View) {
        /**
         * 非阻塞式切换线程(同步执行)
         *
         * CoroutineScope.launch 中我们可以看到接收了一个参数Dispatchers.Main，
         * 这是一个表示协程上下文的参数，用于指定该协程体里的代码运行在哪个线程
         */
        GlobalScope.launch(Dispatchers.Main) {

            val dialog = ProgressDialog(this@MainActivity)
            dialog.setTitle("正在执行非阻塞式切换线程...")
            dialog.show()

            val with = withContext(Dispatchers.IO) {
                //子协程
                Thread.sleep(2000)
                Log.i("testBt1", "${Thread.currentThread().name}")
                "子协程一 \n${Thread.currentThread().name}"
            }

            //主线程
            Log.i("testBt2", "$with")
            dialog.setTitle("$with \n执行完毕")

            val with2 = withContext(Dispatchers.IO) {
                //子线程
                Thread.sleep(3000)
                Log.i("testBt3", "${Thread.currentThread().name}")
                "子协程二 \n${Thread.currentThread().name}"
            }

            dialog.setTitle("$with2 \n执行完毕")

            Log.i("testBt4", "$with2")

            //关闭对话框
            dialog.dismiss()
        }
    }


}
