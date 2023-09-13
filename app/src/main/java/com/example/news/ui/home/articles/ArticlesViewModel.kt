package com.example.news.ui.home.articles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.news.api.ApiManager
import com.example.news.api.articlesModel.Article
import com.example.news.api.articlesModel.ArticlesResponse
import com.example.news.api.sourcesModel.Source
import com.example.news.api.sourcesModel.SourcesResponse
import com.example.news.util.ViewError
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticlesViewModel : ViewModel() {
    val shouldLoad = MutableLiveData<Boolean>(true)
    val articlesList = MutableLiveData<List<Article?>?>()
    val sourcesList = MutableLiveData<List<Source?>?>()
    val errorLiveData = MutableLiveData<ViewError>()
    val shouldDisplayNoArticlesFound = MutableLiveData<Boolean>(false)
    fun getArticles(source: String) {
        shouldLoad.postValue(true)
        ApiManager
            .getApis().getArticles(source = source)
            .enqueue(object : Callback<ArticlesResponse> {
                override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
                ) {
                    shouldLoad.postValue(false)
                    if (response.isSuccessful) {
                        articlesList.postValue(response.body()?.articles)

                        if (response.body()?.articles?.isEmpty() == true)
                            shouldDisplayNoArticlesFound.postValue(true)
                        else
                            shouldDisplayNoArticlesFound.postValue(false)

                    } else {
                        val jsonString = response.errorBody()?.string()
                        val response = Gson().fromJson(jsonString, ArticlesResponse::class.java)
                        errorLiveData.postValue(ViewError(
                            response.message
                        ) {
                            getArticles(source)
                        })
                    }

                }

                override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                    shouldLoad.postValue(false)
                    errorLiveData.postValue(ViewError(t.localizedMessage) {
                        getArticles(source)
                    })

                }

            })
    }


    fun getSources(category: String) {
        shouldLoad.postValue(true)
        ApiManager.getApis().getSources(category = category)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    shouldLoad.postValue(false)
                    if (response.isSuccessful) {
                        sourcesList.postValue(response.body()?.sources)
                    } else {
                        val jsonString = response.errorBody()?.string()
                        val response = Gson().fromJson(jsonString, SourcesResponse::class.java)
                        errorLiveData.postValue(ViewError(response.message) {
                            getSources(category)

                        })

                    }

                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    shouldLoad.postValue(false)
                    errorLiveData.postValue(ViewError(t.localizedMessage) {
                        getSources(category)
                    })
                }

            })
    }

}