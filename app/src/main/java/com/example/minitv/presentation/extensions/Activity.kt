package com.example.minitv.presentation.extensions

import android.app.Activity
import android.util.Log
import java.io.FileNotFoundException


// экстеншн для получения текстового файла с ассетов
fun Activity.getTextAsset(
    path: String,
): String? {
    var text: String? = null
    try {
        applicationContext.assets
            .open(path)
            .bufferedReader()
            .use {
                text = it.readText()
            }
    } catch (e: FileNotFoundException){
        Log.d("Extension getTextAsset", "File $path not found")
    }
    return text
}