package com.example.eatandtell.ui.appmain

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.eatandtell.dto.PostDTO
import com.example.eatandtell.dto.TopTag
import com.example.eatandtell.dto.UserDTO
import com.example.eatandtell.ui.CustomTextField
import com.example.eatandtell.ui.Profile
import com.example.eatandtell.ui.Tag
import com.example.eatandtell.ui.showToast
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchScreen(navController: NavHostController, context: ComponentActivity, viewModel: AppMainViewModel) {
    var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var userLists by remember { mutableStateOf(emptyList<UserDTO>()) }

    var postLists by remember { mutableStateOf(emptyList<PostDTO>()) }

    var loading by remember { mutableStateOf(false) }

    var triggerSearch by remember { mutableStateOf(false) }

    var topTags by remember { mutableStateOf<List<TopTag>>(emptyList()) }




    //searchBar
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),)
    {
        Spacer(modifier = Modifier.height(11.dp))
        SearchBar(
            value = searchText,
            onValueChange = { searchText = it; /*TODO: triggerSearch = true; 이렇게 하니까 한글 입력할 때 자꾸 에러가 남*/},
            onSearchClick = { triggerSearch = true }
        )
        Spacer(modifier = Modifier.height(20.dp))


        LaunchedEffect(Unit) { // LaunchedEffect with Unit to run only once
            viewModel.getTopTags(
                onSuccess = { tags ->
                    topTags = tags.take(5) // Take the first five tags
                },
                onError = { errorMessage ->
                    showToast(context, "Failed to load top tags: $errorMessage")
                }
            )
        }
        // Check if both lists are empty and triggerSearch is false
        DefaultTagView(searchText.text, topTags.map { it.ko_label })

        //search for userLists
        LaunchedEffect(triggerSearch) {
            println("search screen "+searchText.text + " " + triggerSearch)
            if (triggerSearch) {
                postLists = emptyList();
                userLists = emptyList();
                loading = true
                try {
                    if(searchText.text.startsWith("@")) { // If search starts with @
                        if (searchText.text.length>=2) viewModel.getFilteredUsersByName( // 실질 searchtext가 존재하는 경우만 검색
                            searchText.text.drop(1), // Remove @ from the search string
                            onSuccess = { users ->
                                userLists = users // resulted user Lists
                                loading = false
                            }
                        )
                        postLists = emptyList() // Reset post lists
                    }
                    else if(searchText.text.startsWith("#")) {
                        //TODO: search by tags
                        if (searchText.text.length>=2) viewModel.getFilteredUsersByTag(
                            searchText.text.drop(1), // Remove @ from the search string
                            onSuccess = { users ->
                                userLists = users // resulted user Lists
                                loading = false
                            }
                        )
                        postLists = emptyList() // Reset post lists
                    }
                    else {
                        if (searchText.text.length>=1) viewModel.getFilteredByRestaurants(
                            searchText.text,
                            onSuccess = { posts ->
                                postLists = posts // resulted post Lists
                                loading = false
                            }
                        )
                        userLists = emptyList() // Reset user lists
                    }
                } catch (e: Exception) {
                    println("searchload error")
                    showToast(context, "search 로딩에 실패하였습니다")
                    loading = false
                }
                triggerSearch = false // reset the trigger
            }
        }

        if(loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    //로딩 화면
                    modifier = Modifier
                        .size(70.dp)
                )
            }
        }
        else {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.fillMaxSize()
            ) {
                if (searchText.text.startsWith("@") || searchText.text.startsWith("#")) {
                    items(userLists.size) { index ->
                        val user = userLists[index]
                        Profile(
                            profileUrl = user.avatar_url,
                            username = user.username,
                            userDescription = user.description,
                            onClick = {
                                if (user.id == viewModel.myProfile?.id) {
                                    navController.navigate("Profile")
                                } else {
                                    navController.navigate("Profile/${user.id}")
                                }
                            },
                        )
                    }
                } else {
                    items(postLists.size) { index ->
                        val post = postLists[index]
                        HomePost(post, viewModel = viewModel, navHostController = navController)
                    }
                }

                // navigation bottom app bar 때문에 스크롤이 가려지는 것 방지 + 20.dp padding
                item { Spacer(modifier = Modifier.height(70.dp)) }
            }

        }
    }

}

@Composable
fun SearchBar(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, onSearchClick: () -> Unit) {
    CustomTextField(
        value = value.text,
        onValueChange = { onValueChange(TextFieldValue(it)) },
        placeholder = "@: 유저, #: 태그, 없음: 식당 검색",
        trailingIcon = {
            Box(
                modifier = Modifier.clickable(onClick = onSearchClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }
        }
    )
}

@Composable
fun DefaultTagView(text: String, tags: List<String>) {
    if (
        text == "@" ||
        text == "#" ||
        text.isEmpty()
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            tags.forEach { tagName ->
                Tag(tagName)
            }
        }
        Spacer(modifier = Modifier.height(11.dp))
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(navController = rememberNavController(), context = ComponentActivity(), viewModel = AppMainViewModel())
}




