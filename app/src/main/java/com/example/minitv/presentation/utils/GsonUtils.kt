package com.example.minitv.presentation.utils

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonUtils {

    inline fun <reified T> convertToString(json: String, typeToken: Type): T {
        val gson = Gson()
        return gson.fromJson(json, typeToken)
    }
}