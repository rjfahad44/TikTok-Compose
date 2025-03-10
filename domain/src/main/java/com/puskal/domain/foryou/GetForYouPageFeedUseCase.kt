package com.puskal.domain.foryou

import com.puskal.data.model.VideoModel
import com.puskal.data.repository.home.ForYouRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetForYouPageFeedUseCase @Inject constructor(private val forYouRepository: ForYouRepository) {
    operator fun invoke(): Flow<List<VideoModel>> {
        return forYouRepository.getForYouPageFeeds()
    }
}