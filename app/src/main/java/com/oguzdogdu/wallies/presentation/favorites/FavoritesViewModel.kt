package com.oguzdogdu.wallies.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.usecase.favorites.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val getFavoritesUseCase: GetFavoritesUseCase) :
    ViewModel() {

    private val _getFavorites = MutableStateFlow(FavoriteUiState())
    val favorites = _getFavorites.asStateFlow()

    init {
        getFavoritesList()
    }

    private fun getFavoritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoritesUseCase.invoke().collectLatest { result ->
                result.let {
                    _getFavorites.value = FavoriteUiState(favorites = it)
                }
            }
        }
    }
}
