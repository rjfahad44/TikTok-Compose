package com.puskal.cameramedia

import androidx.lifecycle.viewModelScope
import com.puskal.core.base.BaseViewModel
import com.puskal.domain.cameramedia.GetTemplateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraMediaViewModel @Inject constructor(
    private val getTemplateUseCase: GetTemplateUseCase
) : BaseViewModel<ViewState, CameraMediaEvent>() {

    init {
        getTemplates()
    }

    override fun onTriggerEvent(event: CameraMediaEvent) {
        when (event) {
            CameraMediaEvent.EventFetchTemplate -> getTemplates()
        }
    }

    private fun getTemplates() {
        viewModelScope.launch {
            getTemplateUseCase().collect {
                updateState((viewState.value ?: ViewState()).copy(templates = it))
            }
        }
    }


}