package com.oguzdogdu.wallies.presentation.favorites

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.wallies.ui.theme.Typography

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val state by viewModel.favorites.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    Alignment.Center
                ) {
                    Text(text = "Favorites", style = Typography.body1)
                }
            },
            modifier = Modifier.clip(
                shape = RoundedCornerShape(
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                )
            ),
            backgroundColor = Color(0xFFFEDBD0),
            contentColor = Color(0xFF442c2E),
            elevation = 8.dp
        )
    }, content = {
            Box(modifier = Modifier.padding(12.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = rememberLazyGridState(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.favorites) { favorites ->
                        if (favorites != null) {
                            FavoriteItem(favoriteImages = favorites, onItemClick = {
                                favorites.id.let { navigateToDetail.invoke(it) }
                            })
                        }
                    }
                }
            }
        })
}
