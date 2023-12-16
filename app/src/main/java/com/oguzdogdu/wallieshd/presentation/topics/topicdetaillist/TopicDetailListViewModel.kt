package com.oguzdogdu.wallieshd.presentation.topics.topicdetaillist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oguzdogdu.domain.usecase.topics.topicdetail.GetTopicDetailListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TopicDetailListViewModel @Inject constructor(
    private val getTopicDetailListUseCase: GetTopicDetailListUseCase
) : ViewModel() {

    private val _getTopicList = MutableStateFlow<TopicDetailListState?>(null)
    val getTopicList = _getTopicList.asStateFlow()

    fun handleUIEvent(event: TopicDetailListEvent) {
        when (event) {
            is TopicDetailListEvent.FetchTopicListData -> {
                getTopicDetailList(idOrSlug = event.idOrSlug)
            }
        }
    }

    private fun getTopicDetailList(idOrSlug: String?) {
        viewModelScope.launch {
            getTopicDetailListUseCase(idOrSlug = idOrSlug).cachedIn(viewModelScope).collectLatest { topicList ->
                topicList.let { list ->
                    _getTopicList.update { TopicDetailListState.TopicListState(topicList = list) }
                }
            }
        }
    }
}
