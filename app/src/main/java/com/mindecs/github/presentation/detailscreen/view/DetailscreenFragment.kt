package com.mindecs.github.presentation.detailscreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.mindecs.github.R
import com.mindecs.github.common.Constants
import com.mindecs.github.common.NavigationUtil
import com.mindecs.github.common.getRandomColor
import com.mindecs.github.common.hide
import com.mindecs.github.common.utils.setSafeOnClickListener
import com.mindecs.github.databinding.DetailscreenFragmentBinding
import com.mindecs.github.domain.model.Item

class DetailscreenFragment : Fragment() {

    private lateinit var binding: DetailscreenFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailscreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            detailToolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        }

        arguments?.getParcelable<Item>(Constants.ITEM_KEY)?.let { model ->
            binding.apply {

                detailToolbar.setOnMenuItemClickListener { item ->
                    when (item?.itemId) {
                        R.id.go -> NavigationUtil.navigateToBrowser(
                            requireContext(),
                            model.htmlUrl ?: Constants.EMPTY
                        )
                    }
                    true
                }


                val topicList =
                    if (model.topics?.isNotEmpty() == true) "Topics: ${model.topics}" else ""
                desc.text = "${model.description}\n${model.createdAt}\n$topicList"

                authorImg.load(model.owner?.avatarUrl ?: Constants.EMPTY) {
                    placeholder(R.drawable.ic_mark_github_24)
                    error(R.drawable.ic_mark_github_24)
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                model.language?.let {
                    circle.setColorFilter(getRandomColor())
                    language.text = it
                } ?: run {
                    language.hide()
                    circle.hide()
                }
                repositoryName.text = model.name
                authorName.text = model.owner?.login
                issueCount.text = model.openIssuesCount.toString()
                starCount.text = model.watchersCount.toString()
                forkCount.text = model.forksCount.toString()
                authorImg.setSafeOnClickListener {
                    model.owner?.htmlUrl?.let {
                        NavigationUtil.navigateToBrowser(requireContext(), it.trim())
                    }

                }
            }
        }
    }
}