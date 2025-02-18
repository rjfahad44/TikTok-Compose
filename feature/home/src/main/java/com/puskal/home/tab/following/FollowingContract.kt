package com.puskal.home.tab.following

import com.puskal.data.model.ContentCreatorFollowingModel


data class ViewState(
    val isLoading: Boolean? = null,
    val error: String? = null,
    val contentCreators: List<ContentCreatorFollowingModel>? = null
)

sealed class FollowingEvent {
}