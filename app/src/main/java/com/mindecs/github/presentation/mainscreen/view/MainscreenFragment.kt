package com.mindecs.github.presentation.mainscreen.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mindecs.github.R
import com.mindecs.github.common.*
import com.mindecs.github.common.utils.PagingListener
import com.mindecs.github.common.utils.RecyclerPagingScrollListener
import com.mindecs.github.databinding.MainscreenFragmentBinding
import com.mindecs.github.presentation.mainscreen.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class MainscreenFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: MainscreenFragmentBinding

    private val mAdapter: RepositoriesRVAdapter by lazy { RepositoriesRVAdapter() }

    private val viewModel: MainScreenViewModel by viewModels()

    private val onScrollListener by lazy {
        RecyclerPagingScrollListener(pagingListener = object : PagingListener {
            override fun loadMore() {
                if (viewModel.getNextPage) {
                    search()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainscreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        observeData()
    }

    private fun setViews() {
        binding.apply {

            searchToolbar.setOnMenuItemClickListener {
                val popupMenu = PopupMenu(requireContext(), searchToolbar, Gravity.END)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item?.itemId) {
                        R.id.stars -> viewModel.setSortType(MainScreenViewModel.SORT_BY_STARS)
                        R.id.forks -> viewModel.setSortType(MainScreenViewModel.SORT_BY_FORKS)
                        R.id.updated -> viewModel.setSortType(MainScreenViewModel.SORT_BY_UPDATED)
                    }
                    mAdapter.clearList()
                    search()
                    true
                }
                popupMenu.inflate(R.menu.sort_inner_menu)
                popupMenu.show()
                true
            }

            searchBtn.setOnClickListener {
                search()
            }

            mAdapter.setOnItemClickListener {
                findNavController().navigate(
                    R.id.main_to_detail,
                    bundleOf(
                        Constants.ITEM_KEY to it
                    )
                )
            }

            searchView.setOnQueryTextListener(this@MainscreenFragment)
            repositoriesRV.adapter = mAdapter

            repositoriesRV.addOnScrollListener(onScrollListener)

            searchView.clearFocus()
        }
    }

    private fun search() {
        binding.searchView.query?.let {
            val text = it.trim().toString()
            if (text.isNotEmpty()) {
                if (text != viewModel.searchText) {
                    mAdapter.clearList()
                }
                viewModel.search(text)
                hideKeyboard()
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launchWhenResumed {
            viewModel.data.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.loading.show()
                    }
                    is Resource.Success -> {
                        binding.errTxt.hide()
                        binding.repositoriesRV.show()
                        binding.loading.hide()
                        it.data?.items?.let { it1 -> mAdapter.setList(it1) }
                    }
                    is Resource.Error -> {
                        binding.errTxt.hide()
                        binding.errTxt.text = it.message
                        binding.loading.hide()
                    }
                }
            }
        }
    }


    override fun onQueryTextSubmit(p0: String?): Boolean {
        search()
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}