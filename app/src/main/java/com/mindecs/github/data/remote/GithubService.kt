package com.mindecs.github.data.remote

import com.mindecs.github.common.Constants
import com.mindecs.github.domain.model.SearchModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubService {


    @Headers("Accept: application/vnd.github.v3+json")
    @GET("search/repositories")
    suspend fun search(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("per_page") perPage: Int = Constants.DEFAULT_RESULT_PER_PAGE,
        @Query("page") page: Int,
        @Header("Authorization") token: String
    ): SearchModel
}