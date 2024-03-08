package com.oguzdogdu.wallieshd.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.databinding.FragmentSearchBinding
import com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections.UserCollectionsFragment
import com.oguzdogdu.wallieshd.presentation.profiledetail.userphotos.UserPhotosFragment
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchViewModel by activityViewModels()

    private val fragments by lazy {
        listOf(UserPhotosFragment(), UserCollectionsFragment())
    }

    override fun firstExecution(savedInstanceState: Bundle?) {
        super.firstExecution(savedInstanceState)
        viewModel.handleUIEvent(SearchEvent.GetAppLanguageValue)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initViewPager()
        initTabLayout()
        val tag = this.arguments?.getString("tag")
        if (tag?.isNotEmpty() == true) {
            binding.editTextSearchWalpaper.setText(tag)
            viewModel.handleUIEvent(
                SearchEvent.EnteredSearchQuery(tag, viewModel.appLanguage.value)
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.buttonBack.setOnClickListener { navigateBack() }
        searchToImages()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun searchToImages() {
        binding.apply {
            editTextSearchWalpaper.setOnFocusChangeListener { _, hasFocus ->
                tvCancel.isVisible = hasFocus
            }

            editTextSearchWalpaper.textChanges().observeInLifecycle(viewLifecycleOwner) {
                viewModel.handleUIEvent(
                    SearchEvent.EnteredSearchQuery(
                        it.toString(),
                        viewModel.appLanguage.value
                    )
                )
            }

            editTextSearchWalpaper.requestFocus()

            openKeyboardInSearch()

            tvCancel.setOnClickListener {
                tvCancel.isVisible = false
                editTextSearchWalpaper.text?.clear()
                editTextSearchWalpaper.clearFocus()
            }
        }
    }

    private fun openKeyboardInSearch() {
        val im: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.showSoftInput(activity?.currentFocus, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun initTabLayout() {
        val tabTitles =
            listOf(
                getString(R.string.photos),
                getString(R.string.users)
            )

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun initViewPager() {
        val pagerAdapter = SearchScreenViewPagerAdapter(
            requireParentFragment().requireActivity(),
            fragments
        )
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.handleUIEvent(SearchEvent.EnteredSearchQuery("", ""))
    }
}
