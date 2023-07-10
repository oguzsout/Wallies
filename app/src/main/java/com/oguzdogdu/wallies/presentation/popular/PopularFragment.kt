package com.oguzdogdu.wallies.presentation.popular

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(FragmentPopularBinding::inflate) {

    @Inject
    lateinit var connection: CheckConnection

    private val viewModel: PopularViewModel by viewModels()

    private val popularWallpaperAdapter by lazy { PopularWallpaperAdapter() }

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.setupRecyclerView(
                layoutManager = GridLayoutManager(requireContext(), 2),
                adapter = popularWallpaperAdapter,
                true,
                onScroll = {
                    recyclerViewWallpapers.addOnScrollListener(object :
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
        popularWallpaperAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toDetail, arguments)
        }
        binding.swipeRefresh.setOnRefreshListener {
            checkConnection()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
    }

    private fun checkConnection() {
        connection.observe(viewLifecycleOwner) { isConnected ->
            when (isConnected) {
                true -> {
                    viewModel.getPopularImages()
                    viewModel.getPopular.observeInLifecycle(viewLifecycleOwner, observer = { state ->
                        when {
                            state.isLoading -> binding.progressBar.show()

                            state.error.isNotEmpty() -> {}

                            else -> {
                                binding.progressBar.hide()
                                popularWallpaperAdapter.submitData(state.popular)
                            }
                        }
                    })
                }

                false -> {
                    requireView().showToast(requireContext(), R.string.internet_error)
                }

                null -> TODO()
            }
        }
    }
}
