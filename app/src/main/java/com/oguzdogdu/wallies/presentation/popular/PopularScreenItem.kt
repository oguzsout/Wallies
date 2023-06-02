package com.oguzdogdu.wallies.presentation.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.oguzdogdu.domain.model.popular.PopularImage

@Composable
fun PopularScreenItem(
    popularImage: PopularImage,
    onItemClick: (String) -> Unit
) {

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(220.dp)
            .width(140.dp)
            .clickable { onItemClick("") }
    ) {
        AsyncImage(
            model = popularImage.url,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}