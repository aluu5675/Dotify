package com.andyluu.dotify.model

import android.app.Application
import android.widget.Toast
import com.andyluu.dotify.activity.MainActivity
import com.andyluu.dotify.manager.ApiManager
import com.andyluu.dotify.manager.MusicManager
import com.ericchee.songdataprovider.Song

class DotifyApp: Application() {

    lateinit var musicManager: MusicManager
    lateinit var apiManager: ApiManager

    var currentSong: Song? = null

    override fun onCreate() {
        super.onCreate()

        musicManager = MusicManager()
        apiManager = ApiManager(this)

    }

}