package com.oguzdogdu.network.service

import androidx.annotation.VisibleForTesting
import com.oguzdogdu.network.model.maindto.Link
import com.oguzdogdu.network.model.maindto.Sponsorship
import com.oguzdogdu.network.model.maindto.UnsplashResponseItem
import com.oguzdogdu.network.model.maindto.Urls
import com.oguzdogdu.network.model.maindto.User

@VisibleForTesting
fun mockBaseWallpaper() = listOf(
    UnsplashResponseItem(
        altDescription = "the sun is shining through the trees in the forest",
        blurHash = "LMAcuKxt9bNGt7ofj[R*0LRj-oof",
        color = "#26260c\n",
        createdAt = "2023-04-28T13:09:43Z",
        description = "Date palms in Wadi Sharma, Sharma - NEOM, Saudi Arabia | The NEOM Nature Reserve region is being designed to deliver protection and restoration of biodiversity across 95% of NEOM.",
        height = 8180,
        id = "QbDCsB2yCSM",
        likedByUser = false,
        likes = 298,
        links = Link(
            download = "",
            downloadLocation = "",
            html = "https://unsplash.com/@neom",
            self = "https://api.unsplash.com/users/neom"
        ),
        promotedAt = "2023-08-18T18:07:36Z\n",
        sponsorship = Sponsorship(
            sponsor = null,
            tagline = "Made to Change",
            taglineUrl = "https://www.neom.com/en-us?utm_source=unsplash&utm_medium=referral"
        ),
        updatedAt = "",
        urls = Urls(
            full = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=srgb&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=85",
            raw = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3",
            regular = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=80&w=1080",
            small = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=80&w=400",
            thumb = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=80&w=200"
        ),
        user = User(
            true,
            "Located in the northwest of Saudi Arabia, NEOM’s diverse climate offers both sun-soaked beaches and snow-capped mountains. NEOM’s unique location will provide residents with enhanced livability while protecting 95% of the natural landscape.",
            "NEOM",
            false,
            "mYizSrdJkkU",
            "discoverneom",
            null,
            null,
            "NEOM, Saudi Arabia",
            "NEOM",
            "http://www.neom.com",
            null,
            null,
            null,
            null,
            null,
            null, null, null, null,
        ),
        width = 5454,
        views = 1.3534345345346346,
        downloads = 0
    ),
    UnsplashResponseItem(
        altDescription = "the sun is shining through the trees in the forest",
        blurHash = "LMAcuKxt9bNGt7ofj[R*0LRj-oof",
        color = "#26260c\n",
        createdAt = "2023-04-28T13:09:43Z",
        description = "Date palms in Wadi Sharma, Sharma - NEOM, Saudi Arabia | The NEOM Nature Reserve region is being designed to deliver protection and restoration of biodiversity across 95% of NEOM.",
        height = 8180,
        id = "QbDCsB2yCSM",
        likedByUser = false,
        likes = 298,
        links = Link(
            download = "",
            downloadLocation = "",
            html = "https://unsplash.com/@neom",
            self = "https://api.unsplash.com/users/neom"
        ),
        promotedAt = "2023-08-18T18:07:36Z\n",
        sponsorship = Sponsorship(
            sponsor = null,
            tagline = "Made to Change",
            taglineUrl = "https://www.neom.com/en-us?utm_source=unsplash&utm_medium=referral"
        ),
        updatedAt = "",
        urls = Urls(
            full = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=srgb&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=85",
            raw = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3",
            regular = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=80&w=1080",
            small = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=80&w=400",
            thumb = "https://images.unsplash.com/photo-1682687220363-35e4621ed990?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w0NzMwNzN8MXwxfGFsbHwxfHx8fHx8MXx8MTY5MjM4NDgwMXw&ixlib=rb-4.0.3&q=80&w=200"
        ),
        user = User(
            true,
            "Located in the northwest of Saudi Arabia, NEOM’s diverse climate offers both sun-soaked beaches and snow-capped mountains. NEOM’s unique location will provide residents with enhanced livability while protecting 95% of the natural landscape.",
            "NEOM",
            false,
            "mYizSrdJkkU",
            "discoverneom",
            null,
            null,
            "NEOM, Saudi Arabia",
            "NEOM",
            "http://www.neom.com",
            null,
            null,
            null,
            null,
            null,
            null, null, null, null,
        ),
        width = 5454,
        views = 1.3534345345346346,
        downloads = 0
    )
).toString()
