package com.example.networkapp.ui.theme

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.networkapp.data.Post
import com.example.networkapp.data.PostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PostListViewModel(
    private val myRepository: PostData,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ViewModel logic
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            val posts = myRepository.getPosts().first()
            _posts.value = posts
        }
    }

    fun selectPost(post: Post) {
        Log.d("SelectedPost", _selectedPost.toString())
        _selectedPost.value = post
        _isEditing.value = false
    }

    fun allowEditing() {
        _isEditing.value = !_isEditing.value
    }

    fun editTitle(title: String) {
        _selectedPost.value = _selectedPost.value?.copy(title = title)
    }

    fun editText(body: String) {
        _selectedPost.value = _selectedPost.value?.copy(body = body)
    }

    fun savePost(post: Post) {
        if (post != null) {
            viewModelScope.launch {
                val updatedPost = myRepository.updatePost(post).first()
                _selectedPost.value = updatedPost
                _isEditing.value = false
            }
        }
    }

    // Define ViewModel factory in a companion object
    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                // val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return PostListViewModel(
                    PostData(),
                    savedStateHandle
                ) as T
            }
        }
    }
}