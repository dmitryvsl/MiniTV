package com.example.minitv.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.minitv.R
import com.example.minitv.domain.model.TvProgram
import com.example.minitv.presentation.extensions.getTextAsset
import com.example.minitv.presentation.utils.GsonUtils
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import javax.inject.Provider

const val ASSET_PATH_MEDIA_LIST = "Videos/medialist.json"

class MainActivity : AppCompatActivity() {

    private lateinit var rootView: ConstraintLayout

    @Inject
    lateinit var gsonUtils: Provider<GsonUtils>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component.inject(this)

        findViews()

        val mediaList = openAsset(ASSET_PATH_MEDIA_LIST)

        if (mediaList == null) {
            showSnackbar(getString(R.string.file_not_found))
            return
        }

        val tvPrograms: List<TvProgram> = gsonUtils.get().convertToString(mediaList)

        Log.d("MainActivity", "onCreate: $tvPrograms")
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

    private fun findViews(){
        rootView = findViewById(R.id.rootView)
    }


}