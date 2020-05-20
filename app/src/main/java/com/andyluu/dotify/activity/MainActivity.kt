package com.andyluu.dotify.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.andyluu.dotify.R
import com.andyluu.dotify.fragment.NowPlayingFragment
import com.andyluu.dotify.fragment.SongListFragment
import com.andyluu.dotify.manager.MusicManager
import com.andyluu.dotify.model.DotifyApp
import com.andyluu.dotify.model.OnSongClickListener
import com.ericchee.songdataprovider.Song
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnSongClickListener {

    private var nowPlayingFragment: NowPlayingFragment? = null

    private lateinit var songListFragment: SongListFragment

    private var hasBackStack = false

    var allSongs: List<Song> = listOf()

    lateinit var musicManager: MusicManager

    companion object {
        private const val BACK_STACK = "back_stack"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicManager = MusicManager()

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                var currentSong = musicManager.getCurrentSong()
                playerSong.text = "${currentSong?.title} - ${currentSong?.artist}"
                nowPlayingFragment = NowPlayingFragment()
                val argumentBundle = Bundle().apply {
                    putParcelable(NowPlayingFragment.SONG_KEY, currentSong)
                }
                nowPlayingFragment!!.arguments = argumentBundle
                hasBackStack = getBoolean(BACK_STACK)
                if (hasBackStack) {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    miniPlayerContainer.visibility = View.INVISIBLE
                } else {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    miniPlayerContainer.visibility = View.VISIBLE
                }
            }
        }

        if (getSongListFragment() == null) {
            songListFragment = SongListFragment()
            val apiManager = (application as DotifyApp).apiManager
            apiManager.fetchSongs ({ listOfSongs ->
                allSongs = listOfSongs.songs
                songListFragment.updateList(allSongs)
            }, {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            })

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(BACK_STACK, hasBackStack)
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onNavigateUp()
    }

    override fun onSongClicked(song: Song) {
        musicManager.setCurrentSong(song)
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
