package com.oguzdogdu.wallieshd.presentation.collections
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentCollectionsBinding
import com.oguzdogdu.wallieshd.presentation.main.MainActivity
import com.oguzdogdu.wallieshd.util.LoaderAdapter
import com.oguzdogdu.wallieshd.util.MenuAdapter
import com.oguzdogdu.wallieshd.util.hide
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
import com.oguzdogdu.wallieshd.util.show
import com.skydoves.powermenu.CircularEffect
import com.skydoves.powermenu.CustomPowerMenu
import com.skydoves.powermenu.MenuAnimation
import dagger.hilt.android.AndroidEntryPoint

const val POWER_MENU_PERCENTAGE = 0.6F

@AndroidEntryPoint
class CollectionsFragment :
    BaseFragment<FragmentCollectionsBinding>(FragmentCollectionsBinding::inflate) {

    private val viewModel: CollectionViewModel by viewModels()

    private val collectionListAdapter by lazy { CollectionListAdapter() }

    override fun initViews() {
        super.initViews()
        with(binding) {
            recyclerViewCollections.setupRecyclerView(
                layout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
                adapter = collectionListAdapter.withLoadStateFooter(LoaderAdapter()),
                true
            ) {
                recyclerViewCollections.addOnScrollListener(object :
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
            toolbarCollection.setTitle(
                title = getString(R.string.collections_screen_title_text),
                titleStyleRes = R.style.ToolbarTitleText
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        collectionListAdapter.setOnItemClickListener {
            navigateWithDirection(
                CollectionsFragmentDirections.toCollectionsLists(
                    id = it?.id,
                    title = it?.title
                )
            )
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)
            binding.swipeRefresh.isRefreshing = false
        }
        binding.textViewSorted.setOnClickListener {
            adjustFilteredView(it)
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(CollectionScreenEvent.FetchLatestData)
        showCollectionsDatas()
        handlePagingState()
    }

    private fun showCollectionsDatas() {
        viewModel.getCollections.observeInLifecycle(viewLifecycleOwner, observer = { state ->
            when (state) {
                is CollectionState.SortedByTitle -> {
                    collectionListAdapter.submitData(PagingData.empty())
                    state.collections.let {
                        collectionListAdapter.submitData(
                            it
                        )
                    }
                }

                is CollectionState.ItemState -> state.collections.let {
                    collectionListAdapter.submitData(
                        it
                    )
                }

                is CollectionState.SortedByLikes -> {
                    collectionListAdapter.submitData(PagingData.empty())
                    state.collections.let {
                        collectionListAdapter.submitData(
                            it
                        )
                    }
                }

                else -> collectionListAdapter.submitData(PagingData.empty())
            }
        })
    }

    private fun adjustFilteredView(view: View) {
        val alphabeticItem = MenuAdapterItem(
            title = getString(R.string.text_alphabetic_sort),
            MenuAdapterItem.MenuItemType.ALPHABETIC
        )

        val likeItem = MenuAdapterItem(
            title = getString(R.string.text_likes_sort),
            MenuAdapterItem.MenuItemType.LIKE
        )

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val width = screenWidth * POWER_MENU_PERCENTAGE

        val customPowerMenu = CustomPowerMenu.Builder(requireContext(), MenuAdapter())
            .addItem(alphabeticItem)
            .addItem(likeItem)
            .setWidth(width.toInt())
            .setAnimation(MenuAnimation.SHOW_UP_CENTER)
            .setBackgroundAlpha(0.5f)
            .build()

        customPowerMenu.setOnMenuItemClickListener { _, item ->
            when (item.menuItemType) {
                MenuAdapterItem.MenuItemType.ALPHABETIC -> viewModel.handleUIEvent(
                    CollectionScreenEvent.SortByTitles
                )

                MenuAdapterItem.MenuItemType.LIKE -> viewModel.handleUIEvent(
                    CollectionScreenEvent.SortByLikes
                )
            }
        }

        customPowerMenu.circularEffect = CircularEffect.BODY
        customPowerMenu.setAutoDismiss(true)
        customPowerMenu.showAsDropDown(view)
    }

    private fun handlePagingState() {
        collectionListAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.show()
                    binding.recyclerViewCollections.hide()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.hide()
                    binding.recyclerViewCollections.show()
                }
                else -> {}
            }
            val errorState = when {
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                it.error.message?.let { message ->
                    showMessage(message = message, type = MessageType.ERROR)
                }
            }
        }
    }
}
