package com.oguzdogdu.wallieshd.presentation.popular

// @SuppressLint("StateFlowValueCalledInComposition")
// @Composable
// fun PopularScreen(
//    viewmodel: PopularViewModel = hiltViewModel(),
//    navigateToDetail: (String) -> Unit,
// ) {
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
// }
