package com.example.minitv.presentation.mediaplayer

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log
import android.view.SurfaceHolder
import com.example.minitv.domain.model.TvProgram
import com.example.minitv.presentation.main_activity.VIDEO_ASSET_BASE_PATH
import java.io.IOException
import kotlin.math.log

class SurfaceHolderCallback(
    private val context: Context,
    private val tvPrograms: List<TvProgram>,
    private val onPlaybackStartCallback: OnPlayBackStartCallback,
) : SurfaceHolder.Callback {

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var surfaceHolder: SurfaceHolder
    private var descriptor: AssetFileDescriptor? = null

    // необходим для отслеживания индека файла в медиалисте, который должен проигрываться дальше
    private var currentVideoIndex = 0


    // когда текущий видос заканчивается, запускаем следующий
    private val onCompletionListener =
        MediaPlayer.OnCompletionListener {
            resetMediaPlayer()

            currentVideoIndex = calculateNextVideoIndex(currentVideoIndex)
            startVideo(tvPrograms[currentVideoIndex])
        }

    fun clearResources(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        descriptor?.close()
        descriptor = null
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceHolder = holder

        mediaPlayer?.let {
            mediaPlayer?.setDisplay(holder)
            mediaPlayer?.start()
            return@surfaceCreated
        }

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener(onCompletionListener)
        startVideo(tvPrograms[currentVideoIndex])
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mediaPlayer?.pause()
        descriptor?.close()
        descriptor = null
    }

    private fun startVideo(tvProgram: TvProgram) {
        try {
            descriptor = context.resources.assets.openFd(pathCreator(tvProgram.videoName))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        checkNotNull(mediaPlayer)
        val mediaPlayer = mediaPlayer!!

        mediaPlayer.setDataSource(
            descriptor!!.fileDescriptor,
            descriptor!!.startOffset,
            descriptor!!.length
        )
        descriptor!!.close()
        mediaPlayer.setDisplay(surfaceHolder)
        mediaPlayer.prepare()
        mediaPlayer.start()

        onPlaybackStartCallback.playBackStart(tvProgram)
    }

    private fun resetMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener(onCompletionListener)
    }

    private fun pathCreator(fileName: String): String = VIDEO_ASSET_BASE_PATH + fileName

    private fun calculateNextVideoIndex(currentVideoIndex: Int): Int =
        if (currentVideoIndex + 1 >= tvPrograms.size)
            0
        else
            currentVideoIndex + 1
}