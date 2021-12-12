package com.mindecs.github.domain.repository

import com.mindecs.github.domain.model.SearchModel

interface GithubRepository {

    suspend fun searchRepositories(query: String, page: Int, sort: String): SearchModel
}