package com.andyluu.dotify.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andyluu.dotify.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_song.view.*

class SongListAdapter(private var listOfSongs: List<com.andyluu.dotify.model.Song>): RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

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

    fun change(newSongList: List<com.andyluu.dotify.model.Song>) {
        listOfSongs = newSongList
        notifyDataSetChanged()
    }

    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(song: Song) {
            Picasso.get().load(song.smallImageURL).into(itemView.songCover)
            itemView.songTitle.text = song.title
            itemView.songArtist.text = song.artist

            itemView.setOnClickListener() {
                onSongClickListener?.invoke(song)
            }
        }
    }

}