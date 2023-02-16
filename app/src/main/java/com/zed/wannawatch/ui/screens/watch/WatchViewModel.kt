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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.api.models.seapi.ServerResult
import com.zed.wannawatch.services.api.models.seapi.Servers
import com.zed.wannawatch.services.api.models.tmdb.Season
import com.zed.wannawatch.services.models.Movie
import com.zed.wannawatch.services.models.MovieType
import com.zed.wannawatch.services.repository.MovieRepository
import com.zed.wannawatch.services.repository.TMDBRepositoryImpl
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.IOException


class WatchViewModel(
    private val repository: MovieRepository
): ViewModel() {

    companion object {
        val client = OkHttpClient()
        val baseUrl = "https://seapi.link/"
        val jsonDecoder = Json{ ignoreUnknownKeys = true }
    }

    val tmdbRespository = TMDBRepositoryImpl()

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
                        getServers(it.imdbID)
                    }
                }
        }
    }


    /**
     * Opens a browser tab in app
     */
    fun openCustomTab(withUrl: String, context: Context) {
        // https://developer.chrome.com/docs/android/custom-tabs/integration-guide/#opening-a-custom-tab
        val builder = CustomTabsIntent.Builder()


        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(withUrl))
    }


    /**
     * Filter servers based on the quality
     */
    fun filterServers(servers: List<ServerResult>): List<ServerResult> {
        // sorts the servers based on a defined sort order
        // https://stackoverflow.com/a/54097377
        val sortOrder = listOf("HD", "1080p", "720p", "CAM", "?", "")
        return servers.sortedBy {
            sortOrder.indexOf(it.quality)
        }

    }

    /**
     * Fetches available servers for the movie / series
     * if season and episode are not null then it will fetch all the servers for that episode in the season
     */
    fun getServers(imdbId: String, season: Int? = null, episode: Int? = null) {
        serversLoading = true
        val urlBuilder = baseUrl.toHttpUrl().newBuilder()
        // add search query parameter
        urlBuilder.addQueryParameter("type", "imdb")
        urlBuilder.addQueryParameter("id", imdbId.removePrefix("tt"))

        if(season != null && episode != null) {
            urlBuilder.addQueryParameter("season", season.toString())
            urlBuilder.addQueryParameter("episode", episode.toString())
        }


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
                    val json = jsonDecoder.decodeFromString(Servers.serializer(), body)

                    servers.postValue(json)
                }

                serversLoading = false
            }

        })
    }

    fun refreshSeasons(tmdbId: Int) {
        viewModelScope.launch {
            val response = tmdbRespository.getTvDetail(tmdbId)
            response?.let {
                val cpy = model.value?.copy(
                    seriesSeasons = Json.encodeToString(it.seasons)
                )
                cpy?.let { m ->
                    repository.updateMovie(m)
                    model.postValue(m)
                }

            }


        }
    }

}

//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9
class WatchViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WatchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}