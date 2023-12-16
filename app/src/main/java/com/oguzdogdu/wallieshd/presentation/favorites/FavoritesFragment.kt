package com.oguzdogdu.wallieshd.presentation.favorites

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentFavoritesBinding
import com.oguzdogdu.wallieshd.presentation.main.MainActivity
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding>(FragmentFavoritesBinding::inflate) {

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoritesListAdapter by lazy { FavoritesListAdapter() }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        with(binding) {
            recyclerViewFavorites.setupRecyclerView(
                layout = GridLayoutManager(requireContext(), 2),
                adapter = favoritesListAdapter,
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
                is FavoriteUiState.FavoriteError -> showMessage(
                    message = state.errorMessage.orEmpty(),
                    type = MessageType.ERROR
                )

                is FavoriteUiState.Favorites -> {
                    when (state.favorites?.isNotEmpty()) {
                        true -> {
                            binding.progressBar.hide()
                            binding.linearLayoutNoPicture.hide()
                            favoritesListAdapter.submitList(state.favorites)
                        }

                        false -> {
                            binding.recyclerViewFavorites.hide()
                            binding.linearLayoutNoPicture.show()
                            binding.progressBar.hide()
                        }

                        null -> Unit
                    }
                }
                is FavoriteUiState.FavoritesFromFirebase -> {
                    when (state.favorites.isNotEmpty()) {
                        true -> {
                            binding.progressBar.hide()
                            binding.linearLayoutNoPicture.hide()
                            favoritesListAdapter.submitList(state.favorites)
                        }

                        false -> {
                            binding.recyclerViewFavorites.hide()
                            binding.linearLayoutNoPicture.show()
                            binding.progressBar.hide()
                        }
                    }
                }

                else -> {}
            }
        })
    }
}
