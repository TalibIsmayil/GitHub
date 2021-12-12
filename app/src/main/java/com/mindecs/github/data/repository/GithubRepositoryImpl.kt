package com.mindecs.github.data.repository

import com.mindecs.github.BuildConfig
import com.mindecs.github.data.remote.GithubService
import com.mindecs.github.domain.model.SearchModel
import com.mindecs.github.domain.repository.GithubRepository
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api: GithubService
) : GithubRepository {


    override suspend fun searchRepositories(query: String, page: Int, sort: String): SearchModel {
        return api.search(q = query, page = page, sort = sort, token = BuildConfig.ACCESS_TOKEN)
    }
}