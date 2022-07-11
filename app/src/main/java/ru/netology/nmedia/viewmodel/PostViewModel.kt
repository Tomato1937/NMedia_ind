package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

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

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data by repository::data
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
}