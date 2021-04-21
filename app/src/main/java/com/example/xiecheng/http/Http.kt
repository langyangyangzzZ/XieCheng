package com.example.xiecheng.http

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.xiecheng.api.Api
import com.example.xiecheng.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 *    author : Tiaw.
 *    date   : 4/20/21
 *    博客：https://blog.csdn.net/weixin_44819566
 *    desc   :
 */

val TAG = "Http:"


/**
 * 请求网络数据
 */
fun buildHttp(context: Context, textview: TextView) = runBlocking {
    launch {

        //切换为子线程
        val def = async(Dispatchers.IO) {
            //在这个括号里的是子线程，出了这个括号是主线程

            /**
             * TODO 方式一 Retrofit 网络请求
             */
//            HttpRetrofit()

            /**
             * TODO 方式二 OkHttp 请求数据
             */
            val okhttp = HttpOkHttp()
            okhttp
        }

        //主线程    def.await() == okhttp

        //def.await()获取到async最后一行的返回值
        textview.text = "OkHttp请求数据为:\n ${def.await()}"
    }
}


fun Context.toast(title: String) {
    Toast.makeText(this, title, Toast.LENGTH_LONG).show()
}

/**
 * Retrofit 请求数据
 */
fun HttpRetrofit(): String {
    val retrofit = Retrofit.Builder().run {
        baseUrl(Constant.BASE_URL)
        //RxJava切换线程
        addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        addConverterFactory(GsonConverterFactory.create())
        build()

    }
    val data = retrofit.create(Api::class.java)
        .getData()


//    data.enqueue(object : Callback<DataBean> {
//        override fun onResponse(call: Call<DataBean>, response: Response<DataBean>) {
//            Log.i(TAG, "${response.body().toString()}")
//            context.toast("成功")
//            textview.text = response.body()!!.hitokoto
//        }
//
//        override fun onFailure(call: Call<DataBean>, t: Throwable) {
//            context.toast("失败")
//            textview.text = "请求失败"
//            Log.i(TAG, "${t.message}")
//        }
//    })

    return data.execute().body().toString()
}

/**
 * okHttp请求
 */
fun HttpOkHttp(): String {
    //创建OkHttpClient对象
    val okHttpClient = OkHttpClient()

    val request = Request.Builder().run {
        url(Constant.BASE_URL)//请求接口。如果需要传参拼接到接口后面。
        build()//创建Request 对象
    }
    //得到Response 对象
    val execute = okHttpClient.newCall(request).execute()

    return "code = ${execute.code()}\n body = ${execute.body()?.string()}"
}


