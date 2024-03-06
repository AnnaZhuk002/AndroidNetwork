package com.example.networkapp.interfaces

import com.example.networkapp.data.Post
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostAPI {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @PUT("posts/{postId}")
    suspend fun putPost(@Path("postId") id: Int, @Body post: Post): Post

    companion object {
        fun create(): PostAPI {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(PostAPI::class.java)
        }
    }
}