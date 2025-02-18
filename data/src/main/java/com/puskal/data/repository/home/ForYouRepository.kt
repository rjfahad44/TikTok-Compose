package com.puskal.data.repository.home

import com.puskal.data.model.VideoModel
import com.puskal.data.source.VideoDataSource.fetchVideos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ForYouRepository @Inject constructor() {
    fun getForYouPageFeeds(): Flow<List<VideoModel>> {
        return fetchVideos()
    }
}