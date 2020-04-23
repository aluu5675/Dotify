package com.andyluu.dotify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ericchee.songdataprovider.Song
import kotlinx.android.synthetic.main.activity_song_list.view.*
import kotlinx.android.synthetic.main.item_song.view.*

class SongListAdapter (private var listOfSongs: List<Song>): RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    var onSongClickListener: ((song: Song) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(layoutView)
    }

    override fun getItemCount() = listOfSongs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val songTrack = listOfSongs[position]
        holder.bind(songTrack)
    }

    fun change(newSongList: List<Song>) {
        listOfSongs = newSongList
        notifyDataSetChanged()
    }

    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(song: Song) {
            itemView.songCover.setImageResource(song.smallImageID)
            itemView.songTitle.text = song.title
            itemView.songArtist.text = song.artist

            itemView.setOnClickListener() {
                onSongClickListener?.invoke(song)
            }
        }
    }

}