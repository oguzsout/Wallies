package com.oguzdogdu.wallies.presentation.favorites

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentFavoritesBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoritesListAdapter by lazy { FavoritesListAdapter() }

    override fun initViews() {
        super.initViews()
        with(binding) {
            recyclerViewFavorites.setupRecyclerView(
                layout = GridLayoutManager(requireContext(), 2),
                adapter = favoritesListAdapter,
                true,
                onScroll = {
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
            toolbarFavorites.setTitle(
                title = getString(R.string.favorites),
                titleStyleRes = R.style.ToolbarTitleText
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        favoritesListAdapter.setOnItemClickListener {
            navigateWithDirection(FavoritesFragmentDirections.toDetail(id = it?.id))
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(FavoriteScreenEvent.GetFavorites)
        getFavoritesData()
    }

    private fun getFavoritesData() {
        viewModel.favorites.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is FavoriteUiState.Loading -> binding.progressBar.show()
                is FavoriteUiState.FavoriteError -> state.errorMessage?.let {
                    requireView().showToast(
                        context = requireContext(),
                        message = it,
                        duration = Toast.LENGTH_LONG
                    )
                }
                is FavoriteUiState.Favorites -> {
                    when {
                        state.favorites.isEmpty() -> {
                            binding.recyclerViewFavorites.hide()
                            binding.linearLayoutNoPicture.show()
                        }

                        state.favorites.isNotEmpty() -> {
                            binding.progressBar.hide()
                            binding.linearLayoutNoPicture.hide()
                            favoritesListAdapter.submitList(state.favorites)
                        }
                    }
                }

                else -> {}
            }
        })
    }
}
