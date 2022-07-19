package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
//import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = ActivityIntentHandlerBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            } else {
                val text = it.getStringExtra(Intent.EXTRA_TEXT)
                if (text?.isNotBlank() != true) {
                    return@let
                } else {
                    intent.removeExtra(Intent.EXTRA_TEXT)
                    findNavController(R.id.nav_host_fragment).navigate(
                        R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = text
                        }
                    )
                }
            }
        }
    }
}