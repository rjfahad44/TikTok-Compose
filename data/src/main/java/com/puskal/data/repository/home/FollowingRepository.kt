package com.puskal.data.repository.home

import com.puskal.data.model.ContentCreatorFollowingModel
import com.puskal.data.source.ContentCreatorForFollowingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FollowingRepository @Inject constructor() {
    fun getContentCreatorForFollowing(): Flow<List<ContentCreatorFollowingModel>> {
        return ContentCreatorForFollowingDataSource.fetchContentCreatorForFollowing()
    }
}