package ru.netology.nmedia.adapter

import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.util.totalCount

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
    private val SERVER_URL: String = "http://10.0.2.2:9999"
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) = with(binding) {
        tvAuthor.text = post.author
        Glide.with(binding.ivAvatar)
            .load("${SERVER_URL}/avatars/${post.authorAvatar}")
            .circleCrop()
            .placeholder(R.drawable.ic_loading_100dp)
            .error(R.drawable.ic_error_100dp)
            .timeout(10_000)
            .into(binding.ivAvatar)
        tvPublished.text = post.published
        tvContent.text = post.content
        ibMenu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }
                        R.id.edit -> {
                            onInteractionListener.onEdit(post)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        ibLike.isChecked = post.likedByMe
        ibLike.setOnClickListener {
            onInteractionListener.onLike(post)
        }
        ibLike.text = totalCount(("${post.likes}").toInt())
        ibShare.isCheckable=false
        ibShare.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        ibShare.text = totalCount(("${post.shares}").toInt())
    }
}
