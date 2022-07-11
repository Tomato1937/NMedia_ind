package ru.netology.nmedia.util

fun totalCount(count: Int): String {
    return when (count) {
        in 0..999 -> "$count"
        in 1_000..999_999 -> "${((count / 100).toDouble() / 10)}K"
        else -> "${((count / 100_000).toDouble() / 10)}M"
    }
}