package com.example.minitv.presentation.mediaplayer

import com.example.minitv.domain.model.TvProgram

interface OnPlayBackStartCallback {
    fun playBackStart(tvProgram: TvProgram)
}