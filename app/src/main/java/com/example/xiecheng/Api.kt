package com.example.xiecheng

import com.example.xiecheng.bean.DataBean
import retrofit2.Call
import retrofit2.http.GET
import rx.Observable


/**
 *    author : Tiaw.
 *    date   : 4/20/21
 *    博客：https://blog.csdn.net/weixin_44819566
 *    desc   :
 */
interface Api {

    @GET("/")
    fun getData(): Call<DataBean>


}
