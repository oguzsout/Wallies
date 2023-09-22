package com.oguzdogdu.wallieshd.cache

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.oguzdogdu.wallieshd.cache.dao.FavoriteDao
import com.oguzdogdu.wallieshd.cache.database.FavoritesDatabase
import com.oguzdogdu.wallieshd.cache.entity.FavoriteImage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [30])
@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {

    lateinit var favoritesDatabase: FavoritesDatabase
    lateinit var favoriteDao: FavoriteDao

    @Before
    fun setup() {
        favoritesDatabase = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(), FavoritesDatabase::class.java
        ).allowMainThreadQueries().build()
        favoriteDao = favoritesDatabase.favoritesDao
    }

    @Test
    fun insertFavorite() = runBlocking {
        val favorites = FavoriteImage(
            id = "1",
            url = "www.google.com",
            profileImage = "profileImage",
            name = "John",
            portfolioUrl = "www.john.com",
            isChecked = true
        )
        favoriteDao.addFavorites(favorites)
        val result = favoriteDao.getFavorites().first()
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("www.john.com", result[0].portfolioUrl)
    }

    @Test
    fun deleteFavorite() = runBlocking {
        val favorites = FavoriteImage(
            id = "1",
            url = "www.google.com",
            profileImage = "profileImage",
            name = "John",
            portfolioUrl = "www.john.com",
            isChecked = true
        )
        val favoritesSecond = FavoriteImage(
            id = "2",
            url = "www.google.com.tr",
            profileImage = "profileImages",
            name = "Johnathan",
            portfolioUrl = "www.johnathan.com",
            isChecked = false
        )
        favoriteDao.addFavorites(favorites)
        favoriteDao.addFavorites(favoritesSecond)
        favoriteDao.deleteFavorite(favorites)
        val result = favoriteDao.getFavorites().first()
        Assert.assertEquals(false, result[0].isChecked)

    }

    @After
    fun tearDown() {
        favoritesDatabase.close()
    }
}