package com.puskal.domain.creatorprofile

import com.puskal.data.model.VideoModel
import com.puskal.data.repository.creatorprofile.CreatorProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCreatorPublicVideoUseCase @Inject constructor(
    private val creatorProfileRepository: CreatorProfileRepository
) {
    operator fun invoke(id: Long): Flow<List<VideoModel>> {
        return creatorProfileRepository.getCreatorPublicVideo(id)
    }
}