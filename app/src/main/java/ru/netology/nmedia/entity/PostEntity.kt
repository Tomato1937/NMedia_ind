package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
 class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    var shares: Int = 0,
//    val videoUrl: String = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
) {
    fun toDto() = Post(id, author, content, published, likes, likedByMe, shares, /*videoUrl*/)

    companion object {
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.author, dto.content, dto.published,
                    dto.likes, dto.likedByMe, dto.shares, /*dto.videoUrl*/)
    }
}