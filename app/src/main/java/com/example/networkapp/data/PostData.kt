package com.example.networkapp.data

import com.example.networkapp.interfaces.PostAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostData {

    private val postApi = PostAPI.create()

    fun getPosts(): Flow<List<Post>> {
        return flow {
            val posts = postApi.getPosts()
            emit(posts)
        }
    }

    fun updatePost(post: Post): Flow<Post> {
        return flow {
            val puttedPost = postApi.putPost(post.id, post)
            emit(puttedPost)
        }
    }
}