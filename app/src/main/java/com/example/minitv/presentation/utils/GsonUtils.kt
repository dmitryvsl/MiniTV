package com.example.minitv.presentation.utils

import com.google.gson.Gson

class GsonUtils {

    inline fun <reified T> convertToString(json: String): T {
        val gson = Gson()
        return gson.fromJson(json, T::class.java)
    }
}