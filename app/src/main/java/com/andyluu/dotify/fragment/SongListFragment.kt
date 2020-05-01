package com.andyluu.dotify.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andyluu.dotify.R
import com.andyluu.dotify.model.OnSongClickListener
import com.andyluu.dotify.model.SongListAdapter
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import kotlinx.android.synthetic.main.activity_song_list.*
import kotlinx.android.synthetic.main.activity_song_list.rvSong
import kotlinx.android.synthetic.main.fragment_song_list.*

/**
 * A simple [Fragment] subclass.
 */
class SongListFragment : Fragment(), OnSongClickListener {

    private var onSongClickListener: OnSongClickListener? = null

    private var allSongs: List<Song>? = null

    companion object {
        const val ARG_SONGS = "arg_songs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            val allSongs = args.getParcelableArrayList<Song>(ARG_SONGS)
            if (allSongs != null) {
                this.allSongs = allSongs
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSongClickListener) {
            onSongClickListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songAdapter = allSongs?.let { SongListAdapter(it) }
        rvSong.adapter = songAdapter

        if (songAdapter != null) {
            songAdapter.onSongClickListener = {song ->
                onSongClickListener?.onSongClicked(song)
            }
        }

    }

    private fun shuffleList() {

    }

    override fun onSongClicked(song: Song) {

    }
}

