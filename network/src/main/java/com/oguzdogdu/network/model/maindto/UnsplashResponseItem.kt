package com.oguzdogdu.network.model.maindto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.domain.model.home.HomeListItems
import com.oguzdogdu.domain.model.home.HomePopularAndLatest
import com.oguzdogdu.domain.model.latest.LatestImage
import com.oguzdogdu.domain.model.popular.PopularImage
import com.oguzdogdu.domain.model.singlephoto.Photo
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashResponseItem(
    @SerializedName("alt_description")
    val altDescription: String?,
    @SerializedName("blur_hash")
    val blurHash: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("promoted_at")
    val promotedAt: String?,
    @SerializedName("sponsorship")
    val sponsorship: Sponsorship?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("tags")
    val tags: List<Tags?>,
    @SerializedName("user")
    val user: User?,
    @SerializedName("width")
    val width: Int?,
    @SerializedName("views")
    val views: Double?,
    @SerializedName("downloads")
    val downloads: Int?
) : Parcelable {
    @Parcelize
    data class Tags(
        val type: String?,
        val title: String?,
    ) : Parcelable
}
fun UnsplashResponseItem.convertList() = tags.map { it?.title }
fun UnsplashResponseItem.toDomainModelPopular() = PopularImage(id = id, url = urls?.regular)
fun UnsplashResponseItem.toDomainModelLatest() = LatestImage(id = id, url = urls?.regular)
fun UnsplashResponseItem.homieListToDomain(): HomePopularAndLatest {
    val listType = HomePopularAndLatest.ListType.fromType("")

    val homeListItems = HomeListItems(id, urls?.regular)

    return HomePopularAndLatest(Pair(listType?.type,homeListItems))
}

fun UnsplashResponseItem.toDomainModelPhoto() = Photo(
    id = id,
    username = user?.username,
    portfolio = user?.portfolioUrl,
    profileimage = user?.profileImage?.large,
    createdAt = createdAt,
    desc = altDescription,
    urls = urls?.regular,
    views = views,
    downloads = downloads,
    unsplashProfile = user?.links?.html,
    likes = likes,
    bio = user?.bio,
    name = user?.name,
    tag = this.convertList(),
    location = user?.location,
    rawQuality = urls?.raw,
    highQuality = urls?.full,
    mediumQuality = urls?.regular,
    lowQuality = urls?.small
)