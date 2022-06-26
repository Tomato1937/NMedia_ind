package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.util.hideKeyboard

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)

            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post: Post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.etContent) {
                requestFocus()
                setText(post?.content?: "")
            }
            with(binding.ibCancel){
                if (binding.etContent.text != null) {
                    visibility = View.VISIBLE
                } else {
                visibility = GONE
                }
            }
        }

        binding.ibSave.setOnClickListener {
            with(binding.etContent) {
                if (text.isNullOrBlank()) {
                    clearFocus()
                    hideKeyboard()
                    return@setOnClickListener
                } else {
                    viewModel.changeContent(text.toString())
                    viewModel.save()
                    setText("")
                    clearFocus()
                    hideKeyboard()
                }
            }
        }

        binding.ibCancel.setOnClickListener {
            with(binding.etContent) {
                viewModel.clear()
                setText("")
                clearFocus()
                hideKeyboard()
            }
            with(binding.ibCancel) {
                visibility = GONE
            }
        }
    }
}