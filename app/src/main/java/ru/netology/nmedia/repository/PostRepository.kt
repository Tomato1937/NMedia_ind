package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)

    fun getAllAsync(callbacker: GetAllCallBacker<List<Post>>)
    fun removeByIdAsync(id: Long, callbacker: GetAllCallBacker<Unit>)
    fun saveAsync(post: Post, callbacker: GetAllCallBacker<Post>)
    fun likeByIdAsync(id: Long, callbacker: GetAllCallBacker<Post>)

    interface GetAllCallBacker<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}