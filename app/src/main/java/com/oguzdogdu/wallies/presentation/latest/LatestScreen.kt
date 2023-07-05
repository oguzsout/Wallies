package com.oguzdogdu.wallies.presentation.latest

import androidx.compose.runtime.getValue

// @SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
// @Composable
// fun LatestScreen(
//    viewmodel: LatestViewModel = hiltViewModel(),
//    navigateToDetail: (String) -> Unit,
// ) {
//    val stateOfList = viewmodel.getLatest.value.latest.collectAsLazyPagingItems()
//    val isLoading by viewmodel.isLoading.collectAsState()
//    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
//    val listState = rememberLazyGridState()
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
//                itemsPaging(items = stateOfList) { latest ->
//                    LatestScreenItem(latestImage = latest!!, onItemClick = {
//                        latest.id?.let { id -> navigateToDetail.invoke(id) }
//                    })
//                }
//            }
//        }
//    }
// }
