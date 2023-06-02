package com.oguzdogdu.wallies.presentation.popular

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.oguzdogdu.wallies.util.itemsPaging

//@SuppressLint("StateFlowValueCalledInComposition")
//@Composable
//fun PopularScreen(
//    viewmodel: PopularViewModel = hiltViewModel(),
//    navigateToDetail: (String) -> Unit,
//) {
//    val stateOfList = viewmodel.getPopular.value.popular.collectAsLazyPagingItems()
//    val isLoading by viewmodel.isLoading.collectAsState()
//    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
//    val listState = rememberLazyGridState()
//
//
//    SwipeRefresh(state = swipeRefreshState, onRefresh = viewmodel::loadList) {
//        Box {
//            LazyVerticalGrid(
//                modifier = Modifier.padding(8.dp),
//                columns = GridCells.Fixed(2),
//                state = listState,
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                itemsPaging(items = stateOfList) { popular ->
//                    PopularScreenItem(popularImage = popular!!, onItemClick = {
//                        popular.id?.let { id -> navigateToDetail.invoke(id) }
//                    })
//                }
//            }
//        }
//    }
//}