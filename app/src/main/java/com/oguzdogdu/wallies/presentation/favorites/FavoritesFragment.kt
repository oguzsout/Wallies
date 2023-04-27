package com.oguzdogdu.wallies.presentation.favorites

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoritesListAdapter by lazy { FavoritesListAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            val layoutManager =
                GridLayoutManager(requireContext(), 2)
            recyclerViewFavorites.layoutManager = layoutManager
            recyclerViewFavorites.adapter = favoritesListAdapter
            recyclerViewFavorites.setHasFixedSize(true)
        }
    }

    override fun observeData() {
        super.observeData()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favorites.collectLatest { result ->
                    when {
                        result.isLoading -> {}
                        result.favorites.isNotEmpty() -> {
                            favoritesListAdapter.submitList(result.favorites)
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }
}