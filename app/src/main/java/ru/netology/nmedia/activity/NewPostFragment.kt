package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils.hideKeyboard
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let {
            binding.edit.setText(it)
        }

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
                hideKeyboard(requireView())
            }
        }

        binding.edit.requestFocus()

        arguments?.textArg?.let(binding.edit::setText)

        binding.ok.setOnClickListener {
            viewModel.save(binding.edit.text.toString())
            hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }
}