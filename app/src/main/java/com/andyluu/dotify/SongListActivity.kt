package com.andyluu.dotify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import kotlinx.android.synthetic.main.activity_song_list.*

class SongListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        val allSongs: List<Song> = SongDataProvider.getAllSongs()

        val songAdapter = SongListAdapter(allSongs)

        songAdapter.onSongClickListener = { song: Song ->
            val playerSong = findViewById<TextView>(R.id.playerSong)
            playerSong.text = "${song.title} - ${song.artist}"
        }

        btnShuffle.setOnClickListener{
            val newSongList = allSongs.shuffled()

            songAdapter.change(newSongList)
        }

        rvSong.adapter = songAdapter
    }
}
