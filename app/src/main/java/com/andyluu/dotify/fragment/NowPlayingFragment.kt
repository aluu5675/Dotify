package com.andyluu.dotify.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.andyluu.dotify.R
import com.andyluu.dotify.model.Song
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_now_playing.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class NowPlayingFragment : Fragment() {

    private var currentSong: Song? = null

    private var playCount: Int = 0

    companion object {
        val TAG: String = NowPlayingFragment::class.java.simpleName

        private const val PLAY_COUNT = "play_count"

        const val SONG_KEY = "song_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                playCount = getInt(PLAY_COUNT)
            }
        } else {
            // Play count
            playCount = Random.nextInt(1000, 100000)
        }

        arguments?.let { args ->
            val song = args.getParcelable<Song>(SONG_KEY)
            if (song != null) {
                currentSong = song
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPlay.setOnClickListener {
            playCount += 1
            numPlays.text = "${playCount.toString()} plays"
        }

        btnForward.setOnClickListener {
            Toast.makeText(activity, "Skipping to next track", Toast.LENGTH_SHORT).show()
        }

        btnPrevious.setOnClickListener {
            Toast.makeText(activity, "Skipping to previous track", Toast.LENGTH_SHORT).show()
        }

        updateSongViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(PLAY_COUNT, playCount)
        super.onSaveInstanceState(outState)
    }

    fun updateSong(song: com.andyluu.dotify.model.Song) {
        this.currentSong = song
        playCount = Random.nextInt(1000, 100000)
        updateSongViews()
    }

    private fun updateSongViews() {
        currentSong?.let { song ->
            Picasso.get().load(song.largeImageURL).into(trackCover)
            trackTitle.text = song.title
            trackArtist.text = song.artist
            numPlays.text = "${playCount.toString()} plays"
        }
    }

}
