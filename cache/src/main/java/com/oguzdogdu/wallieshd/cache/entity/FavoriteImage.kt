package com.oguzdogdu.wallieshd.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oguzdogdu.domain.model.favorites.FavoriteImages


@Entity(tableName = "favorites")
data class FavoriteImage(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val url: String?,
    val profileImage: String?,
    val name: String?,
    val portfolioUrl: String?,
    var isChecked: Boolean = false,
)

fun FavoriteImage.toDomain() = FavoriteImages(
    id = id,
    url = url,
    profileImage = profileImage,
    name = name,
    portfolioUrl = portfolioUrl,
    isChecked = isChecked
)
