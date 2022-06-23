package ru.netology.nmedia.adapter

import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.totalCount

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) = with(binding) {
        tvAuthor.text = post.author
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
        ibLike.setImageResource(
            if (post.likedByMe) R.drawable.ic_baseline_favorited_border_24
            else R.drawable.ic_baseline_favorite_border_24
        )
        ibLike.setOnClickListener {
            onInteractionListener.onLike(post)
        }
        tvLike.text = totalCount(post.likes)
        ibShare.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        tvShare.text = totalCount(post.shares)
    }
}
