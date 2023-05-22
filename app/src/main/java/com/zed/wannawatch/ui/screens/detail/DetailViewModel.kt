package com.zed.wannawatch.ui.screens.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zed.wannawatch.services.models.movie.Movie
import com.zed.wannawatch.services.models.movie.MovieType
import com.zed.wannawatch.services.models.tmdb.MovieDetailResult
import com.zed.wannawatch.services.models.tmdb.TvDetailResult
import com.zed.wannawatch.services.repository.MovieRepository
import com.zed.wannawatch.services.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val tmdbRepository: TMDBRepository,

    // navigation args are inject through savedstate handler
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _movieState = MutableStateFlow<Movie?>(null)
    val movieState = _movieState.asStateFlow()

    val movieDetailResult = mutableStateOf<MovieDetailResult?>(null)
    val tvDetailResult = mutableStateOf<TvDetailResult?>(null)

    val loading = mutableStateOf(true)

    /**
     * updates the models tmdb id
     */
    private fun updateTmdbId(imdbId: String, modelType: MovieType) = viewModelScope.launch {
        val results = tmdbRepository.findTmdbIdFromImdbId(imdbId)
        println(results)
        results?.let { idResults ->

            when(modelType) {
                MovieType.Movie -> {
                    // check that the item exists
                    if(idResults.movie_results.any()) {
                        // update state
                        _movieState.update {
                            it?.copy(
                                tmdbId = idResults.movie_results.first().id
                            )
                        }
                        println("found tmdb id of: ${idResults.movie_results.first().id} for imdb id: $imdbId")
                    }

                }

                MovieType.Series -> {
                    // check that the item exists
                    if(idResults.tv_results.any()) {
                        // update state
                        _movieState.update {
                            it?.copy(
                                tmdbId = idResults.tv_results.first().id
                            )
                        }
                    }
                }
            }

            // update model in database
            update(_movieState.value)

        }
    }



    fun getMovieDetails() {
        _movieState.value?.let { mov ->
            viewModelScope.launch {

                // 0 is default tmdbid
                if(mov.tmdbId == 0) {
                    println("id was 0, updating...")
                    updateTmdbId(mov.imdbID, mov.resultType)
                }

                val detail = tmdbRepository.getMovieDetail(mov.tmdbId)
                if(detail != null) {
                    movieDetailResult.value = detail
                }

                loading.value = false
            }
        }

    }



    fun getTvDetails() {
        _movieState.value?.let { mov ->
            viewModelScope.launch {
                // 0 is default tmdbid
                if(mov.tmdbId == 0) {
                    println("id was 0, updating...")
                    updateTmdbId(mov.imdbID, mov.resultType)
                }

                val detail = tmdbRepository.getTvDetail(mov.tmdbId)
                if(detail != null) {
                    tvDetailResult.value = detail
                }

                loading.value = false
            }
        }

    }


    fun toggleWatched() {
        _movieState.update {
            it?.copy(
                watched = !it.watched,
                rating = -1,
                notes = ""
            )
        }
        // updates value in DB
        update(_movieState.value)
    }

    fun updateNotesText(text: String) {
        _movieState.update {
            it?.copy(
                notes = text
            )
        }
        update(_movieState.value)
    }

    fun updateRating(ratingValue: Int) {
        _movieState.update {
            it?.copy(
                rating = ratingValue
            )
        }
        update(_movieState.value)
    }

    fun getMovie() = viewModelScope.launch {
        val id = savedStateHandle.get<String>("movieId")
        id?.let {
            val movie = repository.getMovie(it)
            movie.collect { flow ->
                _movieState.update {
                    flow
                }
            }
        }

    }

    private fun update(movie: Movie?) = viewModelScope.launch{
        movie?.let {
            repository.updateMovie(it)
        }
    }

    fun delete(movie: Movie?) = viewModelScope.launch {

        movie?.let {
            repository.deleteMovie(it)

            _movieState.update {
                null
            }
        }



    }
}