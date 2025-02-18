package com.puskal.home.tab.foryou

import com.puskal.data.model.VideoModel


data class ViewState(
    val forYouPageFeed: List<VideoModel>? = null,
    val isLoading: Boolean? = null,
    val error: String? = null,
)

sealed class ForYouEvent {
}