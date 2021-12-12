package com.mindecs.github.domain.usecase

import com.mindecs.github.common.Resource
import com.mindecs.github.domain.model.SearchModel
import com.mindecs.github.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class SearchRepositoriesUseCase @Inject constructor(private val repository: GithubRepository) {

    operator fun invoke(query: String, page: Int, sort: String): Flow<Resource<SearchModel>> =
        flow {
            try {
                emit(Resource.Loading())
                val coins = repository.searchRepositories(query, page, sort)
                emit(Resource.Success(coins))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
            }catch (e: Exception){
                emit(Resource.Error("Something went wrong!"))
            }
        }
}