package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
 class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    var shares: Int = 0
) {
    fun toDto() = Post(id, author, authorAvatar, content, published, likes, likedByMe, shares)

    companion object {
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.author, dto.authorAvatar, dto.content,
            dto.published, dto.likes, dto.likedByMe, dto.shares)
    }
}