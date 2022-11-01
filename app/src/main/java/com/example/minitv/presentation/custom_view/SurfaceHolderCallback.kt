package com.example.minitv.presentation.custom_view

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log
import android.view.SurfaceHolder
import com.example.minitv.domain.model.TvProgram
import com.example.minitv.presentation.main_activity.VIDEO_ASSET_BASE_PATH
import java.io.IOException

class SurfaceHolderCallback(
    private val context: Context,
    private val tvPrograms: List<TvProgram>
) : SurfaceHolder.Callback {

    private var mediaPlayer: MediaPlayer? = null
    private val _mediaPlayer
        get() = mediaPlayer!!
    private lateinit var surfaceHolder: SurfaceHolder
    private var descriptor: AssetFileDescriptor? = null

    // когда текущий видос заканчивается, запускаем следующий
    private val onCompletionListener =
        MediaPlayer.OnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer()
            startVideo(pathCreator(tvPrograms[nextVideoIndex].videoName))
        }

    init {
        mediaPlayer = MediaPlayer()
        _mediaPlayer.setOnCompletionListener(onCompletionListener)
    }

    // необходим для отслеживания индека файла в медиалисте, который должен проигрываться дальше
    private var nextVideoIndex = 0

    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceHolder = holder
        startVideo(VIDEO_ASSET_BASE_PATH + tvPrograms[nextVideoIndex].videoName)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mediaPlayer?.release()
        mediaPlayer = null
        descriptor = null
        Log.d("surfaceDestroyed", "true")
    }

    private fun startVideo(fileName: String) {
        try {
            descriptor = context.resources.assets.openFd(fileName)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        _mediaPlayer.setDataSource(
            descriptor!!.fileDescriptor,
            descriptor!!.startOffset,
            descriptor!!.length
        )
        descriptor!!.close()
        _mediaPlayer.setDisplay(surfaceHolder)
        _mediaPlayer.prepare()
        _mediaPlayer.start()

        updateNextVideoIndex()
    }

    private fun updateNextVideoIndex() {
        // если последний видос, начинаем сначала
        if (nextVideoIndex + 1 < tvPrograms.size) {
            nextVideoIndex++
            return
        }
        nextVideoIndex = 0
    }

    private fun pathCreator(fileName: String): String = VIDEO_ASSET_BASE_PATH + fileName
}