package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private val emptyPost = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false,
    shares = 0,
    videoUrl = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl(AppDb.getInstance(
        application).postDao)
    val data = repository.getAll()
    val edited = MutableLiveData(emptyPost)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun save(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        } else {
            edited.value?.let {
                repository.save(it.copy(content = text))
            }
            edited.value = emptyPost
        }
    }
    fun edit(post: Post) {
        edited.value = post
    }
    fun clear() {
        edited.value?.let{
            edited.value = emptyPost
        }
    }
    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}