package com.oguzdogdu.wallies.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.oguzdogdu.data.common.Constants
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.ui.theme.Typography
import kotlinx.coroutines.launch

val pages = listOf(
    Constants.TAB_POPULAR, Constants.TAB_LATEST
)

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContainerScreen(
    navigateToPopularDetail: (String) -> Unit,
    navigateToLatestDetail: (String) -> Unit,
    navigateToSearch: () -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val tabIndex = pagerState.currentPage
    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = colorResource(id = R.color.background_tab),
            elevation = 0.dp
            ,title = { Text(text = stringResource(R.string.app_name),style = Typography.body2,) }, actions = {
            IconButton(onClick = { navigateToSearch.invoke() }) {
                Icon(
                    painter = painterResource(id = R.drawable.search), contentDescription = "Search"
                )
            }
        })
    }, content = {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = pages.size,
                backgroundColor = colorResource(id = R.color.background_tab),
                indicator = {}) {
                pages.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page = index)
                        }
                    }, text = { Text(text = title) })
                }
            }

            HorizontalPager(
                modifier = Modifier.fillMaxSize(), pageCount = pages.size, state = pagerState
            ) { pager ->
                when (pager) {
//                    0 -> {
//                        PopularScreen(navigateToDetail = {
//                            navigateToPopularDetail.invoke(it)
//                        })
//                    }
//
//                    1 -> {
//                        LatestScreen(navigateToDetail = {
//                            navigateToLatestDetail.invoke(it)
//                        })
//                    }
                }
            }
        }
    })
}