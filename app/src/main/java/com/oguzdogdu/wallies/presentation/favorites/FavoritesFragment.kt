package com.oguzdogdu.wallies.presentation.favorites

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentFavoritesBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoritesListAdapter by lazy { FavoritesListAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewFavorites.setUp(
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

    override fun observeData() {
        super.observeData()
        getFavoritesData()
    }

    private fun getFavoritesData() {
        lifecycleScope.launch {
            viewModel.favorites.onEach { result ->
                when {
                    result.isLoading -> {}
                    result.error.isNotEmpty() -> {}
                    result.favorites.isNotEmpty() -> {
                        favoritesListAdapter.submitList(result.favorites)
                    }
                    else -> {}
                }
            }.observeInLifecycle(this@FavoritesFragment)
        }
    }
}