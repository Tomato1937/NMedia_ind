package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import kotlin.RuntimeException

class PostRepositoryImpl: PostRepository {

    override fun getAll(): List<Post> {
        return PostsApi.retrofitService.getAll()
            .execute()
            .let {
                if (it.isSuccessful) {
                    it.body() ?: error("Body is null")
                } else {
                    throw Exception(it.message())
                }
            }
    }

    override fun getAllAsync(callbacker: PostRepository.GetAllCallBacker<List<Post>>) {
        PostsApi.retrofitService.getAll()
            .enqueue(object: Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    val body = response.body() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    callbacker.onSuccess(body)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callbacker.onError(RuntimeException(t))
                }
            })
    }

    /*override fun likeById(id: Long) {
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
    }*/

    override fun likeByIdAsync(id: Long, callbacker: PostRepository.GetAllCallBacker<Post>) {
        PostsApi.retrofitService.getById(id)
            .enqueue(object: Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    val body = response.body() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    if (body.likedByMe) {
                        PostsApi.retrofitService.unlikeById(id)
                            .enqueue(object: Callback<Post> {
                                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                    val likedBody = response.body() ?: run {
                                        callbacker.onError(RuntimeException("body is null"))
                                        return
                                    }
                                    callbacker.onSuccess(likedBody)
                                }

                                override fun onFailure(call: Call<Post>, t: Throwable) {
                                    callbacker.onError(RuntimeException(t))
                                }
                            })
                    } else {
                        PostsApi.retrofitService.likeById(id)
                            .enqueue(object: Callback<Post> {
                                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                                    val body = response.body() ?: run {
                                        callbacker.onError(RuntimeException("body is null"))
                                        return
                                    }
                                    callbacker.onSuccess(body)
                                }

                                override fun onFailure(call: Call<Post>, t: Throwable) {
                                    callbacker.onError(RuntimeException(t))
                                }
                            })
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callbacker.onError(RuntimeException(t))
                }
            })
    }

    override fun shareById(id: Long) {
//        TODO dao.shareById(id)
    }

    override fun removeById(id: Long) {
        PostsApi.retrofitService.removeById(id)
            .execute()
    }

    override fun removeByIdAsync(id: Long, callbacker: PostRepository.GetAllCallBacker<Unit>) {
        PostsApi.retrofitService.removeById(id)
            .enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    val body = response.body() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    callbacker.onSuccess(body)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callbacker.onError(RuntimeException(t))
                }
            })
    }

    override fun save(post: Post) {
        PostsApi.retrofitService.save(post)
            .execute()
    }

    override fun saveAsync(post: Post, callbacker: PostRepository.GetAllCallBacker<Post>) {
        PostsApi.retrofitService.save(post)
            .enqueue(object: Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    val body = response.body() ?: run {
                        callbacker.onError(RuntimeException("body is null"))
                        return
                    }
                    callbacker.onSuccess(body)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callbacker.onError(RuntimeException(t))
                }
            })
    }
}
