package com.oguzdogdu.wallies.presentation.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentPopularBinding
import com.oguzdogdu.wallies.presentation.favorites.FavoritesScreen
import com.oguzdogdu.wallies.presentation.main.MainActivity
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.hide
import com.oguzdogdu.wallies.util.observe
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.show
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PopularFragment : Fragment() {
    private lateinit var composeView: ComposeView

    @Inject
    lateinit var connection: CheckConnection

    private val viewModel: PopularViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkConnection()
        composeView.setContent {
            PopularScreen( navigateToDetail = {
                val arguments = Bundle().apply {
                    putString("id", it)
                }
                findNavController().navigate(R.id.toDetail,args = arguments)
            })
        }
    }


    private fun checkConnection() {
        connection.observe(viewLifecycleOwner) { isConnected ->
            when (isConnected) {
                true -> {
                    observe(viewModel.getPopular, viewLifecycleOwner) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            when {
                                it.isLoading -> {

                                }

                                it.error.isNotEmpty() -> {}

                                else -> {
                                }
                            }
                        }
                    }
                }

                false -> {
                    requireView().showToast(requireContext(), R.string.internet_error)
                }

                null -> {}
            }
        }
    }
}