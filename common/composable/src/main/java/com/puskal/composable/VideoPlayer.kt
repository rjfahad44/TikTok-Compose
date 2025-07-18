package com.puskal.composable


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.puskal.core.utils.FileUtils
import com.puskal.data.model.VideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    video: VideoModel,
    pagerState: PagerState,
    pageIndex: Int,
    onSingleTap: (exoPlayer: ExoPlayer) -> Unit,
    onDoubleTap: (exoPlayer: ExoPlayer, offset: Offset) -> Unit,
    onVideoDispose: () -> Unit = {},
    onVideoGoBackground: () -> Unit = {}
) {

    val context = LocalContext.current
    var thumbnail by remember {
        mutableStateOf<Pair<Bitmap?, Boolean>>(Pair(null, true))  //bitmap, isShow
    }

//    LaunchedEffect(key1 = true) {
//        withContext(Dispatchers.IO) {
//            val bm = FileUtils.extractThumbnail(
//                context.assets.openFd("videos/${video.videoLink}"), 1
//            )
//            withContext(Dispatchers.Main) {
//                thumbnail = thumbnail.copy(first = bm, second = thumbnail.second)
//            }
//        }
//    }

    if (pagerState.settledPage == pageIndex) {
//        val exoPlayer = remember(context) {
//            ExoPlayer.Builder(context).build().apply {
//                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
//                repeatMode = Player.REPEAT_MODE_ONE
//                setMediaItem(MediaItem.fromUri(Uri.parse("asset:///videos/${video.videoLink}")))
//                playWhenReady = true
//                prepare()
//                addListener(object : Player.Listener {
//                    override fun onRenderedFirstFrame() {
//                        super.onRenderedFirstFrame()
//                        thumbnail = thumbnail.copy(second = false)
//                    }
//                })
//            }
//        }

        val exoPlayer = rememberUniversalPlayer(
            context = context,
            uriString = video.videoLink,
            isPlayWhenReady = true
        )

        val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(key1 = lifecycleOwner) {
            val lifeCycleObserver = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        exoPlayer.pause()
                        onVideoGoBackground()
                    }
                    Lifecycle.Event.ON_START -> exoPlayer.play()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(lifeCycleObserver)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(lifeCycleObserver)
            }
        }

        val playerView = remember {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        DisposableEffect(key1 = AndroidView(factory = {
            playerView
        }, modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                onSingleTap(exoPlayer)
            }, onDoubleTap = { offset ->
                onDoubleTap(exoPlayer, offset)
            })
        }), effect = {
            onDispose {
                thumbnail = thumbnail.copy(second = true)
                exoPlayer.release()
                onVideoDispose()
            }
        })
    }

    if (thumbnail.second) {
        AsyncImage(
            model = thumbnail.first,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

}


@androidx.media3.common.util.UnstableApi
@Composable
fun rememberUniversalPlayer(
    context: Context,
    uriString: String,
    isPlayWhenReady: Boolean = true
): ExoPlayer {

    val player = remember(context) {
        val upstreamFactory = DefaultHttpDataSource.Factory().apply {
            // Optional: setUserAgent("MyApp/${BuildConfig.VERSION_NAME}")
        }
        val dataSourceFactory = DefaultDataSource.Factory(context, upstreamFactory)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .setUsage(C.USAGE_MEDIA)
                        .build(),
                    true
                )

                repeatMode = Player.REPEAT_MODE_ONE
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                setMediaItem(makeMediaItem(uriString))
                playWhenReady = isPlayWhenReady
                prepare()
            }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
    return player
}


private fun makeMediaItem(uriString: String): MediaItem {
    val uri = Uri.parse(uriString)

    // HLS manifests end with .m3u8 – help the parser by tagging the MIME type.
    val mimeType = when {
        uriString.endsWith(".m3u8", ignoreCase = true) -> MimeTypes.APPLICATION_M3U8
        uriString.endsWith(".mp3",  ignoreCase = true) -> MimeTypes.AUDIO_MPEG
        uriString.endsWith(".mp4",  ignoreCase = true) -> MimeTypes.VIDEO_MP4
        else -> null                                   // Let Exo guess
    }

    return if (mimeType == null) {
        MediaItem.fromUri(uri)
    } else {
        MediaItem.Builder()
            .setUri(uri)
            .setMimeType(mimeType)
            .build()
    }
}





