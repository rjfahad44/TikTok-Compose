package com.puskal.creatorprofile.screen.creatorprofile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.puskal.data.model.UserModel
import com.puskal.common.theme.R


data class ViewState(
    val isLoading: Boolean? = null,
    val error: String? = null,
    val creatorProfile: UserModel? = null,
)

sealed class CreatorProfileEvent {
}


enum class ProfilePagerTabs(
    @StringRes val title: Int? = null,
    @DrawableRes val icon: Int
) {
    PUBLIC_VIDEO(icon = R.drawable.ic_list),
    LIKED_VIDEO(icon = R.drawable.ic_private_like)
}