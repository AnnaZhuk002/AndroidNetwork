package com.example.networkapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.networkapp.data.Post
import com.example.networkapp.ui.theme.PostListViewModel


enum class NetworkAppScreen(val route: String) {
    PostsList("postsList"),
    PostView("postView/{postId}")
}

@Composable
fun NetworkApp() {
    val navController = rememberNavController()

    val postViewModel: PostListViewModel = viewModel(
        factory = PostListViewModel.Factory
    )

    // screen logic
    NavHost(navController, startDestination = NetworkAppScreen.PostsList.route) {
        // main screen
        composable(NetworkAppScreen.PostsList.route) {
            Surface {

                val posts by postViewModel.posts.collectAsState()

                ListPosts(
                    posts = posts,
                    onPostClick = { post ->
                        navController.navigate(
                            NetworkAppScreen.PostView.route.replace(
                                "{postId}",
                                post.id.toString()
                            )
                        )
                        val selectedPost = posts.find {it.id == post.id}
                        if (selectedPost != null) {
                            postViewModel.selectPost(selectedPost)
                        }
                    }
                )
            }
        }

        // post screen
        composable(NetworkAppScreen.PostView.route) {
            Surface {
                val isEditing by postViewModel.isEditing.collectAsState()
                val selectedPost by postViewModel.selectedPost.collectAsState()

                if (selectedPost != null) {
                    PostContent(
                        post = selectedPost!!,
                        isEditing = isEditing,
                        onEditClick = { postViewModel.allowEditing() },
                        onTitleChange = { postViewModel.editTitle(it) },
                        onMessageChange = { postViewModel.editText(it) },
                        onSaveClick = { postViewModel.savePost(selectedPost!!) }
                    )
                }
            }
        }
    }
}

@Composable
fun ListPosts(
    posts: List<Post>,
    onPostClick: (Post) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.posts),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        posts.forEach { post ->
            SinglePost(
                post = post,
                onClick = { onPostClick(post) }
            )
        }
    }
}

@Composable
fun SinglePost(
    post: Post,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    )
    {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Post ${post.id}:",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = post.title,
                maxLines = 2
            )
        }
    }
}

@Composable
fun PostContent(
    post: Post,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.content),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        if (isEditing) {
            TextField(
                value = post.title,
                onValueChange = onTitleChange,
                label = { Text(stringResource(R.string.title)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextField(
                value = post.body,
                onValueChange = onMessageChange,
                label = { Text(stringResource(R.string.text)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Button(
                onClick = onSaveClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.save))
            }
        } else {
            Text(
                text = stringResource(R.string.title),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = post.title,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.text),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = post.body,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Button(
                onClick = onEditClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.edit_button))
            }
        }
    }
}