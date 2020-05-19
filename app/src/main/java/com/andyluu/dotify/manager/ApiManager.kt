package com.andyluu.dotify.manager

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.andyluu.dotify.model.AllSongs
import com.google.gson.Gson

class ApiManager(context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun fetchSongs(onSongsReady: (AllSongs) -> Unit, onError: (() -> Unit)? = null) {
        val songsURL = "https://raw.githubusercontent.com/echeeUW/codesnippets/master/musiclibrary.json"

        val request = StringRequest(
            Request.Method.GET, songsURL,
            { response ->
                val gson = Gson()
                val songs = gson.fromJson(response, AllSongs::class.java)

                onSongsReady.invoke(songs)
            },
            {
                onError?.invoke()
            })

        queue.add(request)
    }

    fun fetchArtists() {

    }

    fun fetchUserInfo() {

    }
}