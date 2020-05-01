package com.andyluu.dotify.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.andyluu.dotify.R
import com.andyluu.dotify.fragment.NowPlayingFragment
import com.andyluu.dotify.fragment.SongListFragment
import com.andyluu.dotify.model.OnSongClickListener
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnSongClickListener {

    private var nowPlayingFragment: NowPlayingFragment? = null

    companion object {
        const val SONG_KEY = "song_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val songListFragment = SongListFragment()
        val argumentBundle = Bundle().apply {
            val allSongs: List<Song> = SongDataProvider.getAllSongs()
            val arraySongs: ArrayList<Song> = arrayListOf<Song>()
            arraySongs.addAll(allSongs)
            putParcelableArrayList(SongListFragment.ARG_SONGS, arraySongs)
        }
        songListFragment.arguments = argumentBundle

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragContainer, songListFragment)
            .commit()

        supportFragmentManager.addOnBackStackChangedListener {
            val hasBackStack = supportFragmentManager.backStackEntryCount > 0

            if (hasBackStack) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
    }

    private fun getNowPlayingFragment() = supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) as? NowPlayingFragment

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onNavigateUp()
    }

    override fun onSongClicked(song: Song) {
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
