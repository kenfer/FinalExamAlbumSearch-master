package com.fernandez.finalexam

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import java.util.ArrayList

/**
 * Created by Acer on 12/02/2017.
 */

internal class AlbumsAdapter(private val mContext: Context, private val albums: ArrayList<Album>) : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.song_list_item, parent, false)
        val viewHolder = ViewHolder(v)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AlbumsAdapter.ViewHolder, position: Int) {
        if (!albums[position].getImage().isEmpty()) {
            Glide.with(mContext).load(albums[position].getImage()).into(holder.album_image)
        }
        holder.album_title.setText(albums[position].getName())
        holder.album_artist.setText(albums[position].getArtist())
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val album_image: ImageView
        val album_title: TextView
        val album_artist: TextView

        init {
            album_image = itemView.findViewById(R.id.album_image) as ImageView
            album_title = itemView.findViewById(R.id.album_title) as TextView
            album_artist = itemView.findViewById(R.id.album_artist) as TextView

            itemView.setOnClickListener {
                val albumUri = Uri.parse(albums[adapterPosition].getUrl())
                val websiteIntent = Intent(Intent.ACTION_VIEW, albumUri)
                mContext.startActivity(websiteIntent)
            }

        }
    }
}
