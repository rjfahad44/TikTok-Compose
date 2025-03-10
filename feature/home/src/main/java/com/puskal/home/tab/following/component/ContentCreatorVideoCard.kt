package com.puskal.home.tab.following.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.puskal.composable.VideoPlayer
import com.puskal.core.extension.Space
import com.puskal.data.model.ContentCreatorFollowingModel
import com.puskal.common.theme.R
import com.puskal.theme.White
import com.puskal.theme.WhiteAlpha95
import kotlin.math.absoluteValue



@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun CreatorCard(
    page: Int,
    pagerState: PagerState,
    item: ContentCreatorFollowingModel,
    onClickFollow: (userId: Long) -> Unit,
    onClickUser: (userId: Long) -> Unit
) {
    val pageOffset = (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue
    Card(
        modifier = Modifier.graphicsLayer {
            val scale = lerp(0.9f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
            scaleX = scale
            scaleY = scale
        },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            Modifier
                .height(340.dp)
                .drawWithCache {
                    val color = lerp(
                        Color.Black.copy(alpha = 0.59f),
                        Color.Transparent,
                        1f - pageOffset.coerceIn(0f, 1f)
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(color)
                    }
                }
        )
        {
            VideoPlayer(video = item.coverVideo,
                pagerState = pagerState,
                pageIndex = page,
                onSingleTap = {
                    onClickUser(item.userModel.userId)
                },
                onDoubleTap = { exoPlayer: ExoPlayer, offset: Offset -> },
                onVideoDispose = {},
                onVideoGoBackground = {})

            Icon(
                painterResource(id = R.drawable.ic_cancel),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AsyncImage(
                    model = item.userModel.profilePic,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .size(70.dp)
                        .border(
                            BorderStroke(width = 1.dp, color = White), shape = CircleShape
                        )
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = item.userModel.fullName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Text(
                    text = "@${item.userModel.uniqueUserName}",
                    style = MaterialTheme.typography.labelMedium,
                    color = WhiteAlpha95
                )
                Button(
                    onClick = {
                        onClickFollow(item.userModel.userId)
                    }, modifier = Modifier
                        .padding(top = 2.dp)
                        .padding(horizontal = 36.dp)
                        .fillMaxWidth(), shape = RoundedCornerShape(2.dp)
                ) {
                    Text(text = stringResource(id = R.string.follow))
                }
                12.dp.Space()
            }
            20.dp.Space()
        }
    }
}

