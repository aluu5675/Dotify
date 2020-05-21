package com.andyluu.dotify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.andyluu.dotify.R
import com.andyluu.dotify.fragment.NowPlayingFragment
import com.andyluu.dotify.fragment.SongListFragment
import com.andyluu.dotify.model.DotifyApp
import com.andyluu.dotify.model.OnSongClickListener
import com.andyluu.dotify.model.Song
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnSongClickListener {

    private var nowPlayingFragment: NowPlayingFragment? = null

    private lateinit var songListFragment: SongListFragment

    private var hasBackStack = false

    var allSongs: List<Song> = listOf()

    companion object {
        private const val BACK_STACK = "back_stack"

        private const val ALL_SONGS = "all_songs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                allSongs = this.getParcelableArrayList<Song>(ALL_SONGS)!!
                val fragment = supportFragmentManager.findFragmentByTag(SongListFragment.TAG)
                if (fragment != null) {
                    val argumentBundle = Bundle().apply {
                        putParcelableArrayList(SongListFragment.ARG_SONGS,
                            allSongs as java.util.ArrayList<Song>
                        )
                    }
                    fragment.arguments = argumentBundle
                }
                var currentSong = (application as DotifyApp).musicManager.getCurrentSong()
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
            apiManager.fetchSongs({ listOfSongs ->
                allSongs = listOfSongs.songs
                val argumentBundle = Bundle().apply {
                    putParcelableArrayList(SongListFragment.ARG_SONGS,
                        allSongs as java.util.ArrayList<Song>
                    )
                }
                songListFragment.updateList(allSongs)
                songListFragment.arguments = argumentBundle
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
        val arraySongs: ArrayList<Song> = arrayListOf<Song>()
        arraySongs.addAll(allSongs)
        outState.putParcelableArrayList(ALL_SONGS, arraySongs)
        outState.putBoolean(BACK_STACK, hasBackStack)
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onNavigateUp()
    }

    override fun onSongClicked(song: Song) {
        (application as DotifyApp).musicManager.setCurrentSong(song)
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
