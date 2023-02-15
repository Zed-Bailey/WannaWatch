package com.zed.wannawatch.ui.screens.watch

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.api.models.seapi.Servers
import com.zed.wannawatch.services.api.models.tmdb.Season
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.services.repository.MovieRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.IOException


class WatchViewModel: ViewModel() {

    companion object {
        val client = OkHttpClient()
        val baseUrl = "https://seapi.link/"
    }

    var loading by mutableStateOf(false)
        private set

    val model = MutableLiveData<Movie>()
    val seasons = MutableLiveData<List<Season>?>(null)
    val servers = MutableLiveData<Servers>()

    var serversLoading by mutableStateOf(false)
        private set

    fun clearServers() {
        servers.value = null
    }

    fun getModel(repository: MovieRepository, imdbId: String) {
        viewModelScope.launch {
            repository
                .getMovie(imdbId)
                .collect {
                    model.postValue(it)

                    if(it.seriesSeasons != null) {
                        seasons.postValue(Json.decodeFromString(it.seriesSeasons!!))
                    }

                    if(it.resultType == MovieType.Movie) {
                        getMovieServers(it.imdbID)
                    }
                }
        }
    }


    fun openCustomTab(withUrl: String, context: Context) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(withUrl))
    }

    fun getMovieServers(imdbId: String) {
        serversLoading = true
        val urlBuilder = baseUrl.toHttpUrl().newBuilder()
        // add search query parameter
        urlBuilder.addQueryParameter("type", "imdb")
        urlBuilder.addQueryParameter("id", imdbId.removePrefix("tt"))

        val request = Request.Builder()
            .url(urlBuilder.build().toString())
            .build()

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("com.zed.wannawatch", e.toString())

            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val body = response.body!!.string()
                    val json = Json.decodeFromString(Servers.serializer(), body)

                    servers.postValue(json)
                }
                serversLoading = false
            }

        })
    }

    fun getSeriesServer(imdbId: String, season: Int, episode: Int) {
        serversLoading = true
        val urlBuilder = baseUrl.toHttpUrl().newBuilder()
        // add search query parameter
        urlBuilder.addQueryParameter("type", "imdb")
        urlBuilder.addQueryParameter("id", imdbId.removePrefix("tt"))
        urlBuilder.addQueryParameter("season", season.toString())
        urlBuilder.addQueryParameter("episode", episode.toString())

        val request = Request.Builder()
            .url(urlBuilder.build().toString())
            .build()

        client.newCall(request).enqueue( object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("com.zed.wannawatch", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val body = response.body!!.string()
                    val json = Json.decodeFromString(Servers.serializer(), body)

                    servers.postValue(json)
                }

                serversLoading = false
            }

        })
    }

}