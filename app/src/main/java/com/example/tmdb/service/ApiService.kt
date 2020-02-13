package com.example.tmdb.service

import com.example.tmdb.User
import com.example.tmdb.data.GenreList
import com.example.tmdb.data.Movie
import com.example.tmdb.data.MovieList
import com.example.tmdb.data.SerieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    fun listUsers(): Call<List<User>>

    @GET("genre/movie/list")
    fun listGenres(@Query("api_key") apiKey: String, @Query("language") lang: String): Call<GenreList>

    @GET("movie/popular")
    fun listPopularMovies(@Query("api_key") apiKey: String, @Query("language") lang: String, @Query("page") page:Int): Call<MovieList>

    @GET("tv/airing_today")
    fun listSeriesToday(@Query("api_key") apiKey: String, @Query("language") lang: String, @Query("page") page:Int): Call<SerieList>

    @GET("search/movie")
    fun listSearchResultMovies(@Query("api_key") apiKey: String,
                               @Query("language") lang: String,
                               @Query("query") query: String,
                               @Query("page") page: Int): Call<MovieList>

    @GET("discover/movie")
    fun listMoviesByGenre(@Query("api_key") apiKey: String,
                          @Query("language") lang: String,
                          @Query("sort_by")sortBy:String,
                          @Query("include_adult")includeAdult:String,
                          @Query("include_video")includeVideo:String,
                          @Query("with_genres") genreId:Int,
                          @Query("page") page:Int):Call<MovieList>

    @GET("movie/{id}")
    fun getMovieById(@Path("id") movieId: Int,
                     @Query("api_key") apiKey: String,
                     @Query("language") lang: String):Call<Movie>

}