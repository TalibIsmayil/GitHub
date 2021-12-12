package com.mindecs.github.presentation.mainscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindecs.github.common.Constants
import com.mindecs.github.common.Resource
import com.mindecs.github.domain.model.SearchModel
import com.mindecs.github.domain.usecase.SearchRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val useCase: SearchRepositoriesUseCase
) : ViewModel() {

    private val _data = MutableStateFlow<Resource<SearchModel>?>(null)
    val data = _data.asStateFlow()

    private val getTotalPageCount get(): Int = totalItemCount / Constants.DEFAULT_RESULT_PER_PAGE

    val getNextPage get() = (getTotalPageCount > currentPage + 1) && !isLoading


    var searchText = Constants.EMPTY

    private var currentPage = 1
    private var totalItemCount = 0
    private var isLoading = true

    private var DEFAULT_SORT = SORT_BY_STARS

    fun search(query: String) {
        searchText = query
        useCase.invoke(query, currentPage, DEFAULT_SORT).onEach { result ->

            when (result) {
                is Resource.Loading -> isLoading = true
                is Resource.Success -> {
                    isLoading = false
                    result.data?.totalCount?.let {
                        totalItemCount = it
                    }
                }
                else -> {
                    isLoading = false
                }
            }


            _data.emit(result)

        }.launchIn(viewModelScope)
        currentPage += 1
    }

    fun setSortType(type: String) {
        DEFAULT_SORT = type
    }

    companion object{
        const val SORT_BY_STARS = "stars"
        const val SORT_BY_FORKS = "forks"
        const val SORT_BY_UPDATED = "updated"
    }


}