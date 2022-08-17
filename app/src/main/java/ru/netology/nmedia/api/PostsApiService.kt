package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:9999/api/"

private val okHttpClient = OkHttpClient.Builder()
    .let {
        if (BuildConfig.DEBUG) {
            it.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        } else {
            it
        }
    }
    .connectTimeout(5, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .build()

interface PostsApiService {

    @GET("posts")
    fun getAll(): Call<List<Post>>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>

    @DELETE("posts/{postId}/")
    fun removeById(@Path("postId") postId: Long): Call<Unit>

    @POST("posts/{postId}/likes")
    fun likeById(@Path("postId") postId: Long): Call<Post>

    @DELETE("posts/{postId}/likes")
    fun unlikeById(@Path("postId") postId: Long): Call<Post>

    @GET("posts/{postId}")
    fun getById(@Path("postId") postId: Long): Call<Post>
}
object PostsApi {
    val retrofitService: PostsApiService by lazy {
        retrofit.create()   //retrofit.create(PostsApiService::class.java)
    }
}