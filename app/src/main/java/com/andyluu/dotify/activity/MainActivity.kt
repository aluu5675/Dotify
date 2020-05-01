package com.andyluu.dotify.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.andyluu.dotify.R
import com.andyluu.dotify.fragment.NowPlayingFragment
import com.andyluu.dotify.fragment.SongListFragment
import com.andyluu.dotify.model.OnSongClickListener
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), OnSongClickListener {

    private var nowPlayingFragment: NowPlayingFragment? = null

    private lateinit var songListFragment: SongListFragment

    private var hasBackStack = false

    private var currentSong: Song? = null

    companion object {
        private const val BACK_STACK = "back_stack"

        private const val CURRENT_SONG = "current_song"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                currentSong = getParcelable(CURRENT_SONG)
                currentSong?.let { onSongClicked(it) }
            }
        }

        if (getSongListFragment() == null) {
            songListFragment = SongListFragment()
            val argumentBundle = Bundle().apply {
                val allSongs: List<Song> = SongDataProvider.getAllSongs()
                val arraySongs: ArrayList<Song> = arrayListOf<Song>()
                arraySongs.addAll(allSongs)
                putParcelableArrayList(SongListFragment.ARG_SONGS, arraySongs)
            }
            songListFragment.arguments = argumentBundle

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragContainer, songListFragment, SongListFragment.TAG)
                .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            hasBackStack = supportFragmentManager.backStackEntryCount > 0

            if (hasBackStack) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                miniPlayerContainer.visibility = View.INVISIBLE
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                miniPlayerContainer.visibility = View.VISIBLE
            }
        }

        miniPlayerContainer.setOnClickListener {
            if (nowPlayingFragment != null) {
                miniPlayerContainer.visibility = View.INVISIBLE
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragContainer, nowPlayingFragment!!, NowPlayingFragment.TAG)
                    .addToBackStack(NowPlayingFragment.TAG)
                    .commit()
            }
        }

        btnShuffle.setOnClickListener {
            songListFragment.shuffleList()
        }
    }

    private fun getSongListFragment() = supportFragmentManager.findFragmentByTag(SongListFragment.TAG) as? SongListFragment

    private fun getNowPlayingFragment() = supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) as? NowPlayingFragment

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable(CURRENT_SONG, currentSong)

        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onNavigateUp()
    }

    override fun onSongClicked(song: Song) {
        currentSong = song
        playerSong.text = "${song.title} - ${song.artist}"
        nowPlayingFragment = getNowPlayingFragment()
        if (nowPlayingFragment == null) {
            nowPlayingFragment = NowPlayingFragment()
            val argumentBundle = Bundle().apply {
                putParcelable(NowPlayingFragment.SONG_KEY, song)
            }
            nowPlayingFragment!!.arguments = argumentBundle
        } else {
            nowPlayingFragment!!.updateSong(song)
        }
    }
}
