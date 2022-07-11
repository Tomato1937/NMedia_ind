package ru.netology.nmedia.adapter

import android.view.View
import android.view.View.GONE
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.util.totalCount

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
        ibLike.isChecked = post.likedByMe
        ibLike.setOnClickListener {
            onInteractionListener.onLike(post)
        }
        ibLike.text = totalCount(post.likes)
        ibShare.isCheckable=false
        ibShare.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        ibShare.text = totalCount(post.shares)

        if (post.videoUrl.isNullOrBlank()) {
            ivVideoPreview.visibility = GONE
            playButton.visibility = GONE
        } else {
            ivVideoPreview.setImageResource(R.color.purple_500)
            playButton.visibility = View.VISIBLE
        }

        playButton.setOnClickListener {
            onInteractionListener.onPlayVideo(post)
        }

        ivVideoPreview.setOnClickListener {
            onInteractionListener.onPlayVideo(post)
        }
    }
}
