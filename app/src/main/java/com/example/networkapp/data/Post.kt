package com.example.networkapp.data

// Specified here https://jsonplaceholder.typicode.com/posts/1
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)