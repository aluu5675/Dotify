package com.andyluu.dotify.manager

import com.andyluu.dotify.model.Song

class MusicManager {

    private var currentSong: Song? = null

    fun getCurrentSong(): Song? {
        return currentSong
    }

    fun nextSong() {

    }

    fun prevSong() {

    }

    fun play() {

    }

    fun pause() {

    }

    fun isPlaying () {

    }

    fun setCurrentSong(song: Song) {
        currentSong = song
    }
}