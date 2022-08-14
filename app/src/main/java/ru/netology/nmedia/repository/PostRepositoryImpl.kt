package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.connection.RealCall
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val typeTokenPost = object : TypeToken<Post>() {}

    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999"
        val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let { gson.fromJson(it, typeToken.type) }
    }

    override fun getAllAsync(callbacker: PostRepository.GetAllCallBacker<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

//        client.newCall(request)
        RealCall(client, request, forWebSocket = false)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    try {
                        callbacker.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callbacker.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callbacker.onError(e)
                }
            })
    }

    override fun likeById(id: Long) {
        val requestToGetPost: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        val post: Post = client.newCall(requestToGetPost)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let { gson.fromJson(it, typeTokenPost.type) }

        val requestLikeStatusChange: Request =
            if (post.likedByMe) {
                Request.Builder()
                    .delete()
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .build()
            } else {
                Request.Builder()
                    .post(gson.toJson(post).toRequestBody(jsonType))
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .build()
            }

        client.newCall(requestLikeStatusChange)
            .execute()
            .close()
    }

    override fun likeByIdAsync(id: Long, callbacker: PostRepository.GetAllCallBacker<Post>) {
        val requestToGetPost: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(requestToGetPost)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    val post : Post = gson.fromJson(body, typeTokenPost.type)
                    try {
                        val requestLikeStatusChange: Request =
                            if (post.likedByMe) {
                                Request.Builder()
                                    .delete()
                                    .url("${BASE_URL}/api/posts/$id/likes")
                                    .build()
                            } else {
                                Request.Builder()
                                    .post(gson.toJson(post).toRequestBody(jsonType))
                                    .url("${BASE_URL}/api/posts/$id/likes")
                                    .build()
                            }

                        client.newCall(requestLikeStatusChange)
                            .enqueue(object : Callback {
                                override fun onResponse(call: Call, response: Response) {
                                    try {
                                         callbacker.onSuccess(post)
                                    } catch (e: Exception) {
                                        callbacker.onError(e)
                                    }
                                }
                                override fun onFailure(call: Call, e: IOException) {
                                    callbacker.onError(e)
                                }
                            })
                        callbacker.onSuccess(post)

                    } catch (e: Exception) {
                        callbacker.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callbacker.onError(e)
                }
            })
    }

    override fun shareById(id: Long) {
//        TODO dao.shareById(id)
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeByIdAsync(id: Long, callbacker: PostRepository.GetAllCallBacker<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    callbacker.onSuccess(gson.fromJson(body, typeToken.type))
                }

                override fun onFailure(call: Call, e: IOException) {
                    callbacker.onError(e)
                }
            })
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun saveAsync(post: Post, callbacker: PostRepository.GetAllCallBacker<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    try {
                        callbacker.onSuccess(gson.fromJson(body, typeTokenPost.type))
                    } catch (e: Exception) {
                        callbacker.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callbacker.onError(e)
                }
            })
    }
}
