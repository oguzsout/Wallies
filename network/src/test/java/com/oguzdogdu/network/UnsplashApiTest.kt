package com.oguzdogdu.network

import com.google.gson.Gson
import com.oguzdogdu.network.common.Constants
import com.oguzdogdu.network.model.maindto.UnsplashResponseItem
import com.oguzdogdu.network.service.WallpaperService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class UnsplashApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var wallpaperService: WallpaperService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        wallpaperService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WallpaperService::class.java)
    }

    @Test
    fun `test Api Returns Empty For No Photos With Http 200`() = runTest {
        val wallpapers = emptyList<UnsplashResponseItem>()
        val mockResponse = MockResponse()
        mockResponse.setBody(Gson().toJson(wallpapers)).setResponseCode(HttpURLConnection.HTTP_OK)
        mockWebServer.enqueue(mockResponse)

        val response = wallpaperService.getImagesByOrders(perPage = Constants.PAGE_ITEM_LIMIT,page = 1, order = "")
        mockWebServer.takeRequest()

        Assert.assertEquals(true, response.body()!!.isEmpty())
    }

    @Test
    fun `test Api Returns Photos With Http 200`() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = wallpaperService.getImagesByOrders(perPage = Constants.PAGE_ITEM_LIMIT,page = 1, order = "")
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.body()!!.isEmpty())
    }

    @Test
    fun `test Api With Http 404`() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something Went Wrong")
        mockWebServer.enqueue(mockResponse)

        val response = wallpaperService.getImagesByOrders(perPage = Constants.PAGE_ITEM_LIMIT,page = 1, order = "")
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isSuccessful)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}