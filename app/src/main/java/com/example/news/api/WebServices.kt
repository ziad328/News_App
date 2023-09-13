package com.example.news.api

import com.example.news.api.articlesModel.ArticlesResponse
import com.example.news.api.sourcesModel.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("/v2/top-headlines/sources")
    fun getSources(
        @Query("apiKey") apiKey: String = ApiConstants.API_KEY,
        @Query("category") category: String
    ): Call<SourcesResponse>

    @GET("/v2/everything")
    fun getArticles(
        @Query("apiKey") apiKey: String = ApiConstants.API_KEY,
        @Query("q") searchKeyWord: String? = null,
        @Query("sources") source: String? = null
    ): Call<ArticlesResponse>
}