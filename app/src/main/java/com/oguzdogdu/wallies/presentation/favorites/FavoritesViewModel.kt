package com.oguzdogdu.wallies.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.favorites.GetFavoritesUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val useCase: GetFavoritesUseCase) :
    ViewModel() {

    private val _getFavorites = MutableStateFlow(FavoriteState())
    val favorites: StateFlow<FavoriteState>
        get() = _getFavorites

    init {
        getFavoritesList()
    }

    private fun getFavoritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _getFavorites.value = FavoriteState(isLoading = true)

                    is Resource.Success -> {
                        result.data.let {
                            _getFavorites.value = FavoriteState(favorites = it)
                        }
                    }

                    is Resource.Error -> {
                        _getFavorites.value = FavoriteState(error = result.errorMessage ?: "")
                    }

                    else -> {}
                }
            }
        }
    }
}