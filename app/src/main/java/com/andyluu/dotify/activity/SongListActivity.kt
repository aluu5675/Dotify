package com.andyluu.dotify.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.andyluu.dotify.R
import com.andyluu.dotify.model.SongListAdapter
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import kotlinx.android.synthetic.main.activity_song_list.*

class SongListActivity : AppCompatActivity() {

    private var currentSong: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        val allSongs: List<Song> = SongDataProvider.getAllSongs()

        val songAdapter = SongListAdapter(allSongs)

        songAdapter.onSongClickListener = { song: Song ->
            currentSong = song
            val playerSong = findViewById<TextView>(R.id.playerSong)
            playerSong.text = "${song.title} - ${song.artist}"
        }

        btnShuffle.setOnClickListener{
            val newSongList = allSongs.shuffled()

            songAdapter.change(newSongList)
        }

        playerSong.setOnClickListener {
            if (currentSong != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("song_key", currentSong)

                startActivity(intent)
            }
        }

        rvSong.adapter = songAdapter
    }
}
