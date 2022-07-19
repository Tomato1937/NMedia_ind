package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.util.totalCount
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment: Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.longArg: Long by LongArg
    }
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )
        with(binding.postFragment
         ) {
            viewModel.data.observe(viewLifecycleOwner) { posts ->
                val post = posts.find { it.id == arguments?.longArg }
                if (post != null) {
                    tvAuthor.text = post.author
                    tvPublished.text = post.published
                    tvContent.text = post.content
                    ibMenu.setOnClickListener {
                        PopupMenu(it.context, it).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.remove -> {
                                        findNavController().navigateUp()
                                        viewModel.removeById(post.id)
                                        true
                                    }
                                    R.id.edit -> {
                                        viewModel.edit(post)
                                        findNavController().navigate(
                                            R.id.action_postFragment_to_newPostFragment,
                                            Bundle().apply {
                                                textArg = post.content
                                            }
                                        )
                                        true
                                    }
                                    else -> false
                                }
                            }
                        }.show()
                    }
                    ibLike.isChecked = post.likedByMe
                    ibLike.setOnClickListener {
                        viewModel.likeById(post.id)
                    }
                    ibLike.text = totalCount(post.likes)
                    ibShare.isCheckable=false
                    ibShare.setOnClickListener {
                        viewModel.shareById(post.id)
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, post.content)
                            type = "text/plain"
                        }
                        val shareIntent =
                            Intent.createChooser(intent, getString(R.string.chooser_share_post))
                        startActivity(shareIntent)
                    }
                    ibShare.text = totalCount(post.shares)

                    if (post.videoUrl.isNullOrBlank()) {
                        ivVideoPreview.visibility = View.GONE
                        playButton.visibility = View.GONE
                    } else {
                        ivVideoPreview.visibility = View.VISIBLE
                        ivVideoPreview.setImageResource(R.color.purple_500)
                        playButton.visibility = View.VISIBLE
                    }

                    playButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                        startActivity(intent)
                    }

                    ivVideoPreview.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                        startActivity(intent)
                    }
                }
            }
        }

        return binding.root
    }
}