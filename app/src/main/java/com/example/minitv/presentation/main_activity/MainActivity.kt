package com.example.minitv.presentation.main_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import com.example.minitv.R
import com.example.minitv.domain.model.TvProgram
import com.example.minitv.presentation.App
import com.example.minitv.presentation.extensions.getTextAsset
import com.example.minitv.presentation.utils.GsonUtils
import com.google.android.material.snackbar.Snackbar
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.example.minitv.domain.model.Report
import com.example.minitv.domain.use_cases.UseCase
import com.example.minitv.presentation.mediaplayer.OnPlayBackStartCallback
import com.example.minitv.presentation.mediaplayer.SurfaceHolderCallback
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Provider

const val VIDEO_ASSET_BASE_PATH = "Videos/"
const val ASSET_PATH_MEDIA_LIST = VIDEO_ASSET_BASE_PATH + "medialist.json"

class MainActivity : AppCompatActivity() {

    private lateinit var rootView: FrameLayout
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var surfaceHolderCallback: SurfaceHolderCallback
    private lateinit var tvPrograms: List<TvProgram>

    @Inject
    lateinit var gsonUtils: Provider<GsonUtils>

    // нужен для инжекта в ViewModel
    @Inject
    lateinit var useCase: UseCase

    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.provideFactory(useCase)
    }

    private val onPlayBackStartCallback = object : OnPlayBackStartCallback {
        override fun playBackStart(tvProgram: TvProgram) {
            val report = Report(
                id = 0,
                videoId = tvProgram.videoId,
                videoName = tvProgram.videoName,
                startTime = Calendar.getInstance().time
            )
            viewModel.saveReport(report)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // устанавливаем флаг для отрисовки приложения под system bar'ами
        WindowCompat.setDecorFitsSystemWindows(window, false)

        App.component.inject(this)

        initViews()
        rootView.keepScreenOn = true

        val mediaList = openAsset(ASSET_PATH_MEDIA_LIST)

        if (mediaList == null) {
            showSnackbar(getString(R.string.file_not_found))
            return
        }

        val typeToken = object : TypeToken<List<TvProgram>>() {}.type
        tvPrograms = gsonUtils.get()
            .convertToString<List<TvProgram>>(mediaList, typeToken)
            .sortedBy { tvProgram -> tvProgram.orderNumber }

        surfaceHolder = surfaceView.holder
        surfaceHolderCallback = SurfaceHolderCallback(
            this,
            tvPrograms,
            onPlayBackStartCallback
        )
        surfaceHolder.addCallback(surfaceHolderCallback)
    }


    private fun openAsset(path: String): String? {
        return getTextAsset(path)
    }

    private fun showSnackbar(message: String) {
        Snackbar
            .make(rootView, message, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun initViews() {
        rootView = findViewById(R.id.rootView)
        surfaceView = findViewById(R.id.videoPlayer)
    }

    override fun onDestroy() {
        surfaceHolderCallback.clearResources()
        super.onDestroy()
    }

}