package com.example.minitv.presentation.main_activity

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.minitv.R
import com.example.minitv.domain.model.TvProgram
import com.example.minitv.presentation.App
import com.example.minitv.presentation.extensions.getTextAsset
import com.example.minitv.presentation.utils.GsonUtils
import com.google.android.material.snackbar.Snackbar
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.example.minitv.domain.use_cases.UseCase
import com.example.minitv.presentation.custom_view.SurfaceHolderCallback
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Provider

const val VIDEO_ASSET_BASE_PATH = "Videos/"
const val ASSET_PATH_MEDIA_LIST = VIDEO_ASSET_BASE_PATH + "medialist.json"

class MainActivity : AppCompatActivity() {

    private lateinit var rootView: ConstraintLayout
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var surfaceHolderCallback: SurfaceHolderCallback

    @Inject
    lateinit var gsonUtils: Provider<GsonUtils>

    // нужен для инжекта в ViewModel
    @Inject
    lateinit var useCase: UseCase

    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.provideFactory(useCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        WindowCompat.setDecorFitsSystemWindows(window, false)

        App.component.inject(this)

        findViews()

        val mediaList = openAsset(ASSET_PATH_MEDIA_LIST)

        if (mediaList == null) {
            showSnackbar(getString(R.string.file_not_found))
            return
        }

        val typeToken = object : TypeToken<List<TvProgram>>() {}.type
        val tvPrograms: List<TvProgram> = gsonUtils.get()
            .convertToString<List<TvProgram>>(mediaList, typeToken)
            .sortedBy { tvProgram -> tvProgram.orderNumber }

        surfaceHolder = surfaceView.holder
        surfaceHolderCallback = SurfaceHolderCallback(this, tvPrograms)
        surfaceHolder.addCallback(surfaceHolderCallback)

    }


    private fun openAsset(path: String): String? {
        val mediaList = getTextAsset(path)
        Log.d("MainActivity", "openAsset: $mediaList")
        return mediaList
    }

    private fun showSnackbar(message: String) {
        Snackbar
            .make(rootView, message, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun findViews() {
        rootView = findViewById(R.id.rootView)
        surfaceView = findViewById(R.id.videoPlayer)
    }


}