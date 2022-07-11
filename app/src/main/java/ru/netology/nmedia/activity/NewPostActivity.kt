package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val input = intent?.getStringExtra(Intent.EXTRA_TEXT) ?: ""

        val viewModel: PostViewModel by viewModels()
        binding.ibCancel.setOnClickListener {
            with(binding.ibCancel) {
                if (binding.edit.text.isNullOrBlank()) {
                    visibility = INVISIBLE
                } else {
                    visibility = View.VISIBLE
                }
            }
            with(binding.edit) {
                viewModel.clear()
                setText("")
                clearFocus()
                hideKeyboard()
            }
            finish()
        }

        binding.edit.setText(input)

        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }
}