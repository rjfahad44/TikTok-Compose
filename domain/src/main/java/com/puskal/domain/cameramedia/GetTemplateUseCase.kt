package com.puskal.domain.cameramedia

import com.puskal.data.model.TemplateModel
import com.puskal.data.repository.camermedia.TemplateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTemplateUseCase @Inject constructor(
    private val templateRepository: TemplateRepository
) {
    operator fun invoke(): Flow<List<TemplateModel>> {
        return templateRepository.getTemplates()
    }
}