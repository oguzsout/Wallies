package com.oguzdogdu.wallies.presentation.latest

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLatestBinding
import com.oguzdogdu.wallies.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LatestFragment : BaseFragment<FragmentLatestBinding>(FragmentLatestBinding::inflate) {

    @Inject
    lateinit var connection : CheckConnection

    private val viewModel: LatestViewModel by viewModels()

    private val latestWallpaperAdapter = LatestWallpaperAdapter()

    override fun initViews() {
        super.initViews()
        binding.apply {
            recyclerViewWallpapers.adapter = latestWallpaperAdapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewWallpapers.layoutManager = layoutManager
            recyclerViewWallpapers.setHasFixedSize(true)
        }
    }

    override fun initListeners() {
        super.initListeners()
        latestWallpaperAdapter.setOnItemClickListener {
            val arguments = Bundle().apply {
                putString("id", it?.id)
            }
            navigate(R.id.toDetail,arguments)
        }
    }

    private fun checkConnection(){
        connection.observe(this@LatestFragment) { isConnected ->
            if (isConnected == true) {

            } else {
                requireView().showToast(requireContext(),R.string.internet_error)
            }
        }
    }

    override fun observeData() {
        super.observeData()
        checkConnection()
        lifecycleScope.launch {
            viewModel.getLatest.onEach { result ->
                when {
                    result.isLoading -> {
                        binding.progressBar.show()
                    }
                    result.error.isNotEmpty() -> {
                    }
                    else -> {
                        binding.progressBar.hide()
                        latestWallpaperAdapter.submitData(result.latest)
                    }
                }
            }.observeInLifecycle(this@LatestFragment)
        }
    }


}