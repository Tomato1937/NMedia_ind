package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
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

        getAll()
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

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }
}
