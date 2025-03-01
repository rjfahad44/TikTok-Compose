package com.puskal.domain.creatorprofile

import com.puskal.data.model.UserModel
import com.puskal.data.repository.creatorprofile.CreatorProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCreatorProfileUseCase @Inject constructor(
    private val creatorProfileRepository: CreatorProfileRepository
) {
    operator fun invoke(id: Long): Flow<UserModel?> {
        return creatorProfileRepository.getCreatorDetails(id)
    }
}