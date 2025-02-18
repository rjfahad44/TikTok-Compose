package com.puskal.domain.comment

import com.puskal.data.model.CommentList
import com.puskal.data.repository.comment.CommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCommentOnVideoUseCase @Inject constructor(private val commentRepository: CommentRepository) {
    operator fun invoke(videoId: String): Flow<CommentList> {
        return commentRepository.getComment(videoId)
    }
}