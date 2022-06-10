package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false
        )
        with(binding) {
            tvAuthor.text = post.author
            tvPublished.text = post.published
            tvContent.text = post.content
            if (post.likedByMe) {
                ibLike?.setImageResource(R.drawable.ic_baseline_favorited_border_24)
            }
            tvLike?.text = post.likes.toString()
            tvShare?.text = post.shares.toString()

            root.setOnClickListener {
                Log.d("stuff", "stuff")
            }

            ivAvatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }

            ibLike?.setOnClickListener {
                Log.d("stuff", "like")
                post.likedByMe = !post.likedByMe
                ibLike.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorited_border_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                tvLike.text = totalCount(post.likes)
            }

            ibShare?.setOnClickListener {
                Log.d("stuff", "share")
                post.shares++
                tvShare.text = totalCount(post.shares)
            }
        }
    }
}