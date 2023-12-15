package com.oguzdogdu.wallieshd.presentation.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.topics.GetTopicsListWithPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val getTopicsListWithPagingUseCase: GetTopicsListWithPagingUseCase
) : ViewModel() {
    private val _getTopics = MutableStateFlow<TopicsState?>(null)
    val getTopics = _getTopics.asStateFlow()

    init {
        getTopicsList()
    }

    fun handleUIEvent(event: TopicsScreenEvent) {
        when (event) {
            is TopicsScreenEvent.FetchTopicsData -> {
                getTopicsList()
            }
        }
    }

    private fun getTopicsList() {
        viewModelScope.launch {
            getTopicsListWithPagingUseCase().cachedIn(viewModelScope).collectLatest { topics ->
                _getTopics.update { TopicsState.TopicsListState(topics = topics) }
            }
        }
    }
}
