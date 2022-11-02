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

private const val SKIP_TIME = 5000
private const val TAG = "SurfaceHolderCallback"

class SurfaceHolderCallback(
    private val context: Context,
    private val tvPrograms: List<TvProgram>,
    private val onPlaybackStartCallback: OnPlayBackStartCallback,
) : SurfaceHolder.Callback {

    companion object {
        // константы для сохранения проигрываемого видоса и его места проигрывания в хэшмапе
        const val MEDIA_PLAYER_VIDEO_POSITION = "video_position"
        const val MEDIA_PLAYER_PLAYLIST_POSITION = "playlist_position"
    }

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

    fun skipToEndOfVideo() {
        // проверка, что mediaPlayer не null
        mediaPlayer ?: return

        val mediaPlayer = mediaPlayer!!
        // прежде чем скипнуть проверяем, что длительность видоса не меньше SKIP_TIME
        val skipTime: Int =
            if (mediaPlayer.duration - SKIP_TIME <= 0) mediaPlayer.duration else mediaPlayer.duration - SKIP_TIME
        // скипаем до нужного момента
        mediaPlayer.seekTo(skipTime)
    }

    fun skipToEndOfPlayList() {
        mediaPlayer ?: return

        val mediaPlayer = mediaPlayer!!
        if (!mediaPlayer.isPlaying) return

        // Небольшой костыль
        // тк startVideo предполагает, что видео идут по порядку
        // и каждый раз к currentVideoIndex прибавляет единицу
        // устаналивем индекс равный "Размер_Массива - 2"
        // -2 тк:
        // отнимаем первую единицу тк индексация массива начинается с 0
        // отнимаем вторую единицу тк нужно установить значение переменной на предпредпоследний элемент массива,
        // чтобы после срабатывания метода startVideo переменная указывала на последний элемент массива
        currentVideoIndex = calculateNextVideoIndex(tvPrograms.size - 2)

        resetMediaPlayer()
        startVideo(tvPrograms[tvPrograms.size - 1])
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
        Log.d(TAG, "surfaceDestroyed")
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