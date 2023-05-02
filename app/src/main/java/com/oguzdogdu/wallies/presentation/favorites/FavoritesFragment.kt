package com.oguzdogdu.wallies.presentation.favorites

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentFavoritesBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observe
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoritesListAdapter by lazy { FavoritesListAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewFavorites.setupRecyclerView(
                layoutManager = GridLayoutManager(requireContext(), 2),
                adapter = favoritesListAdapter,
                true, onScroll = {
                    recyclerViewFavorites.addOnScrollListener(object :
                        RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            if (dy > 0) {
                                (activity as MainActivity).slideDown()
                            } else if (dy < 0) {
                                (activity as MainActivity).slideUp()
                            }
                        }
                    })
                }
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        favoritesListAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toDetail, arguments)
        }
    }

    override fun observeData() {
        super.observeData()
        getFavoritesData()
    }

    private fun getFavoritesData() {
        observe(viewModel.favorites, viewLifecycleOwner) {
            when {
                it.isLoading -> {}
                it.error.isNotEmpty() -> {}
                it.favorites.isEmpty() -> {
                    binding.linearLayoutNoPicture.show()
                }
                else -> {
                    binding.linearLayoutNoPicture.hide()
                    favoritesListAdapter.submitList(it.favorites)
                }
            }
        }
    }
}