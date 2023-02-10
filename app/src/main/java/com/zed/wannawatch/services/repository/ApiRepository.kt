package com.zed.wannawatch.services.repository

import android.util.Log
import com.google.gson.Gson
import com.zed.wannawatch.BuildConfig
import com.zed.wannawatch.services.api.models.SearchData
import com.zed.wannawatch.services.api.models.SearchDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiRepository {

    companion object {
        private val client = OkHttpClient()
        private const val baseUrl = "https://omdbapi.com/"

        // fetches the api key from the buildconfig, apikey is stored in gradle.properties
        private const val apiKey = BuildConfig.OMDBApiKey
    }

    suspend fun searchRequest(query: String): Result<SearchData> {
        val urlBuilder = baseUrl.toHttpUrl().newBuilder()
        // add search query parameter
        urlBuilder.addQueryParameter("apikey", apiKey)
        urlBuilder.addQueryParameter("s", query)

        val request = Request.Builder()
            .url(urlBuilder.build().toString())
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                response.close()
                Result.failure(Exception("Error fetching data"))
            } else {
                // decode json
                val body = response.body!!.string()
                Log.i("com.zed.wannawatch" , body)
                val data = Gson().fromJson(body, SearchData::class.java)
                response.close()
                Result.success(data)
            }
        }
    }

    suspend fun searchDetail(imdbId: String): Result<SearchDetail> {
        val urlBuilder = baseUrl.toHttpUrl().newBuilder()
        // add search query parameter
        urlBuilder.addQueryParameter("apikey", apiKey)
        urlBuilder.addQueryParameter("i", imdbId)

        val request = Request.Builder()
            .url(urlBuilder.build().toString())
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                response.close()
                Result.failure(Exception("Error fetching data"))
            } else {
                // decode json
                val body = response.body!!.string()
                Log.i("com.zed.wannawatch" , body)
                val data = Gson().fromJson(body, SearchDetail::class.java)
                response.close()
                Result.success(data)
            }
        }
    }

}