package ru.netology.nmedia.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.totalCount

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            tvAuthor.text = post.author
            tvPublished.text = post.published
            tvContent.text = post.content
            ibLike.setImageResource(
                if (post.likedByMe) R.drawable.ic_baseline_favorited_border_24
                else R.drawable.ic_baseline_favorite_border_24
            )
            ibLike.setOnClickListener{
                onLikeListener(post)
            }
            ibShare.setOnClickListener {
                onShareListener(post)
            }
            tvLike.text = totalCount(post.likes)
            tvShare.text = totalCount(post.shares)
        }
    }
}